/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.populators

import com.github.reline.jisho.dictmodels.jmdict.Dictionary
import com.github.reline.jisho.dictmodels.jmdict.Entry
import com.github.reline.jisho.dictmodels.okurigana.Rubies
import com.github.reline.jisho.dictmodels.okurigana.OkuriganaEntry
import com.github.reline.jisho.jdbcSqliteDriver
import com.github.reline.jisho.requireFile
import com.github.reline.jisho.skipBom
import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.touch
import com.tickaroo.tikxml.TikXml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeToSequence
import okio.IOException
import okio.buffer
import okio.source
import org.slf4j.LoggerFactory
import java.io.File

internal val logger by lazy { LoggerFactory.getLogger("prepopulator-logger") }

interface DictionaryInput {
    val definitions: File
    val okurigana: File
}

suspend fun File.populate(
    inputs: List<DictionaryInput>,
    kanji: Collection<File>,
    radk: Collection<File>,
    krad: Collection<File>,
) = withContext(Dispatchers.IO) { // todo: inject dispatcher
    touch()
    jdbcSqliteDriver.use { driver ->
        val database = JishoDatabase(driver).also { JishoDatabase.Schema.create(driver) }

        val dicts = inputs.map { dictionary ->
            database.populate(
                dictionaryFile = dictionary.definitions,
                okuriganaFile = dictionary.okurigana,
            )
        }
        database.populate(dicts, kanji = kanji, radk = radk, krad = krad)
    }
}

suspend fun JishoDatabase.populate(dictionaryFile: File, okuriganaFile: File): Dictionary {
    requireFile(dictionaryFile)
    requireFile(okuriganaFile)
    val dictionary = extractDictionary(dictionaryFile)
    logger.debug("Populating database with ${dictionaryFile.nameWithoutExtension}")
    insertDictionary(dictionary)
    logger.debug("Populating database with ${okuriganaFile.nameWithoutExtension}")
    extractAndInsertOkurigana(okuriganaFile, dictionary)
    return dictionary
}

@Deprecated(
    "Populate with okurigana instead",
    ReplaceWith("this.populate(dictionaryFile, okuriganaFile)")
)
suspend fun JishoDatabase.populate(dicts: Collection<File>) = runBlocking {
    return@runBlocking dicts.mapNotNull { dict ->
        if (!dict.exists()) return@mapNotNull null
        val dictionary = extractDictionary(dict)
        insertDictionary(dictionary)
        return@mapNotNull dictionary
    }
}

@Throws(IOException::class)
private fun extractDictionary(file: File): Dictionary {
    try {
        file.inputStream().source().buffer().use {
            return TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
                .read(it, Dictionary::class.java)
        }
    } catch (e: IOException) {
        throw IOException("Failed to read ${file.name}", e)
    }
}

private suspend fun JishoDatabase.insertEntries(entries: List<Entry>) = coroutineScope {
    logger.debug("Inserting entries...")
    transaction {
        entries.forEach { entry ->
            ensureActive()
            entryQueries.insert(
                entry.id,
                entry.isCommon(),
                entry.kanji?.firstOrNull()?.value,
                entry.readings.first().value,
            )
            entry.kanji?.forEach { kanji ->
                ensureActive()
                japaneseQueries.insert(entry.id, kanji.value)
            }
            entry.readings.forEach { reading ->
                ensureActive()
                readingQueries.insert(entry.id, reading.value)
            }
        }
    }
    logger.debug("Inserted ${entries.size} entries")
}

private suspend fun JishoDatabase.insertPartsOfSpeech(entries: List<Entry>) = coroutineScope {
    logger.debug("Inserting parts of speech...")
    val count = transactionWithResult {
        val partsOfSpeech = entries
            .flatMap { it.senses }
            .mapNotNull { it.partsOfSpeech }
            .flatten()
            .map { it.decoded() }
        partsOfSpeech.forEach {
            ensureActive()
            partOfSpeechQueries.insert(it)
        }
        partsOfSpeech.count()
    }
    logger.debug("Inserted $count parts of speech")
}

private suspend fun JishoDatabase.insertSenses(entries: List<Entry>) = coroutineScope {
    logger.debug("Inserting senses...")
    val senses = transactionWithResult {
        entries.flatMap { entry ->
            entry.senses.map { sense ->
                ensureActive()
                // todo: can the id be returned from insert to improve performance?
                senseQueries.insert(entry.id)
                val senseId = utilQueries.lastInsertRowId().executeAsOne()
                sense.glosses?.forEach { gloss ->
                    ensureActive()
                    glossQueries.insert(senseId, gloss.value)
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
                            partOfSpeechQueries.selectPosIdWhereValueEquals(pos.decoded())
                                    .executeAsOne()
                    )
                } ?: emptyList()
            }
        }.count()
    }
    logger.debug("Inserted $executions records")
}

private suspend fun JishoDatabase.insertDictionary(dictionary: Dictionary) {
    val entries = dictionary.entries

    insertEntries(entries)
    insertPartsOfSpeech(entries)
    insertSenses(entries)
}

/**
 * See okurigana.json
 */
@OptIn(ExperimentalSerializationApi::class)
@Throws(IOException::class)
private suspend fun JishoDatabase.extractAndInsertOkurigana(file: File, dictionary: Dictionary) =
    coroutineScope {
        val dict = dictionary.entries.associateBy { Rubies.from(it) }
        logger.debug("Extracting okurigana from ${file.name}...")
        try {
            file.inputStream().source().buffer().use { source ->
                source.skipBom()
                transaction {
                    Json.decodeToSequence<OkuriganaEntry>(source.inputStream()).forEach { rubies ->
                        val entry = dict[Rubies(rubies)] ?: return@forEach
                        rubies.furigana.forEach { okurigana ->
                            ensureActive()
                            rubyQueries.insert(entry.id, okurigana.ruby, okurigana.rt)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            throw IOException("Failed to read ${file.name}", e)
        }
        logger.debug("Finished inserting records from ${file.name}")
    }
