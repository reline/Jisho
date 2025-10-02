/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.populators

import com.github.reline.jisho.dictmodels.jmdict.Entry
import com.github.reline.jisho.dictmodels.jmdict.JMdict
import com.github.reline.jisho.dictmodels.jmdict.decodeDictionary
import com.github.reline.jisho.dictmodels.okurigana.OkuriganaEntry
import com.github.reline.jisho.dictmodels.okurigana.decodeOkurigana
import com.github.reline.jisho.jdbcSqliteDriver
import com.github.reline.jisho.requireFile
import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.touch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import org.slf4j.LoggerFactory
import java.io.File

internal val logger by lazy { LoggerFactory.getLogger("prepopulator-logger") }

interface DictionaryInput {
    val definitions: File
    val okurigana: File
}

class JishoInput(
    val dictionaries: List<DictionaryInput>,
    val kanji: Collection<File>,
    val radicals: Collection<File>, /* radk */
)

class JishoPopulator(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun populate(
        databaseFile: File,
        input: JishoInput,
    ) = withContext(dispatcher) {
        databaseFile.touch() // todo: is this required?
        val jdbcSqliteDriver = databaseFile.jdbcSqliteDriver
        jdbcSqliteDriver.use { driver ->
            val database = JishoDatabase(driver).also { JishoDatabase.Schema.create(driver) }

            input.dictionaries.forEach { dictionary ->
                ensureActive()
                database.populate(
                    dictionaryFile = dictionary.definitions,
                    okuriganaFile = dictionary.okurigana,
                )
            }
            database.populate(kanji = input.kanji, radk = input.radicals, krad = emptyList())
        }
    }
}

@Deprecated(
    "Deprecated in favor of dependency injection",
    replaceWith = ReplaceWith("JishoPopulator#populate"),
)
suspend fun File.populate(
    inputs: List<DictionaryInput>,
    kanji: Collection<File>,
    radk: Collection<File>,
    krad: Collection<File>,
) = JishoPopulator().populate(this, JishoInput(inputs, kanji, radk))

suspend fun JishoDatabase.populate(dictionaryFile: File, okuriganaFile: File) {
    logger.debug("Populating database with ${dictionaryFile.nameWithoutExtension}")
    requireFile(dictionaryFile)
    dictionaryFile.source().buffer().use { source ->
        insertDictionary(decodeDictionary(source))
    }

    logger.debug("Populating database with ${okuriganaFile.nameWithoutExtension}")
    requireFile(okuriganaFile)
    okuriganaFile.source().buffer().use { source ->
        insertOkurigana(decodeOkurigana(source))
    }
}

suspend fun JishoDatabase.insertEntries(entries: List<Entry>) = coroutineScope {
    logger.debug("Inserting entries...")
    transaction {
        entries.forEach { entry ->
            ensureActive()
            entryQueries.insert(entry.id, entry.isCommon())
            // todo: consider inserting only one of each the kanji & reading (which ones?)
            entry.kanji?.forEach { kanji ->
                ensureActive()
                japaneseQueries.insert(kanji.value)
                // todo: use upsert or lastInsertRowId()
                val id = japaneseQueries.select(kanji.value).executeAsOne()
                japaneseQueries.insertRelationship(entry_id = entry.id, japanese_id = id)
            }
            entry.readings.forEach { reading ->
                ensureActive()
                readingQueries.insert(reading.value)
                // todo: use upsert or lastInsertRowId()
                val id = readingQueries.select(reading.value).executeAsOne()
                readingQueries.insertRelationship(entry_id = entry.id, reading_id = id)
            }
        }
    }
    logger.debug("Inserted ${entries.size} entries")
}

suspend fun JishoDatabase.insertPartsOfSpeech(entries: List<Entry>) = coroutineScope {
    logger.debug("Inserting parts of speech...")
    transaction {
        entries.asSequence()
            .flatMap { it.senses }
            .mapNotNull { it.partsOfSpeech }
            .flatten()
            .distinct()
            .forEach {
                ensureActive()
                partOfSpeechQueries.insert(it)
            }
    }
    logger.debug("Inserted parts of speech")
}

suspend fun JishoDatabase.insertSenses(entries: List<Entry>) = coroutineScope {
    logger.debug("Inserting senses...")
    val senses = transactionWithResult {
        entries.flatMap { entry ->
            entry.senses.map { sense ->
                ensureActive()
                // todo: research using upsert (`RETURNING` clause)
                //  but only for populating db, not supported on android yet
                senseQueries.insert(entry.id)
                val senseId = utilQueries.lastInsertRowId().executeAsOne()
                sense.glosses?.forEach { gloss ->
                    ensureActive()
                    // todo: make gloss values unique (normalize database table)
                    glossQueries.insert(senseId, gloss)
                }
                senseId
            }
        }
    }
    logger.debug("Inserted ${senses.size} senses")

    val iter = senses.iterator()

    logger.debug("Associating senses with part of speech...")
    val executions = transactionWithResult {
        entries.flatMap { entry ->
            entry.senses.flatMap { sense ->
                val senseId = iter.next()
                sense.partsOfSpeech?.map { pos ->
                    ensureActive()
                    sensePosTagQueries.insert(
                            senseId,
                            partOfSpeechQueries.selectPosIdWhereValueEquals(pos)
                                    .executeAsOne()
                    )
                } ?: emptyList()
            }
        }.count()
    }
    logger.debug("Inserted $executions records")
}

suspend fun JishoDatabase.insertDictionary(dictionary: JMdict) = with(dictionary) {
    insertEntries(entries)
    insertPartsOfSpeech(entries)
    insertSenses(entries)
}

suspend fun JishoDatabase.insertOkurigana(
    okurigana: Sequence<OkuriganaEntry>,
) = coroutineScope {
    data class RubyId(val readingId: Long, val japaneseId: Long?)
    data class Ruby(val reading: String, val kanji: String?)

    val entries = entryQueries.selectReadings().executeAsList()
        .associate {
            Ruby(reading = it.reading, kanji = it.kanji) to
                    RubyId(readingId = it.reading_id, japaneseId = it.japanese_id)
        }

    logger.info("${entries.size} unique combinations")

    transaction {
        var previousInsertRowId = -1L
        okurigana.forEach { (kanji, reading, furigana) ->
            ensureActive()
            val entry = entries[Ruby(reading = reading, kanji = kanji)]
            if (entry?.japaneseId == null) {
                logger.warn("No matching entry found for $kanji ($reading)")
                return@forEach
            }

            furigana.forEachIndexed { i, (ruby, rt) ->
                ensureActive()
                rubyQueries.insert(japanese = ruby, okurigana = rt)
                val lastInsertRowId = utilQueries.lastInsertRowId().executeAsOne()
                val rubyId = if (previousInsertRowId == lastInsertRowId) {
                    rubyQueries.selectRuby(japanese = ruby, okurigana = rt).executeAsOne()
                } else {
                    previousInsertRowId = lastInsertRowId
                    lastInsertRowId
                }
                rubyQueries.insertRelationship(
                    japanese_id = entry.japaneseId,
                    reading_id = entry.readingId,
                    ruby_id = rubyId,
                    position = i.toLong(),
                )
            }
        }
    }
}
