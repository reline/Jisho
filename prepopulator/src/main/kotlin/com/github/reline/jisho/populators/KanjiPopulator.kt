/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.populators

import com.github.reline.jisho.dictmodels.Radk
import com.github.reline.jisho.dictmodels.decodeRadicals
import com.github.reline.jisho.dictmodels.jmdict.Dictionary
import com.github.reline.jisho.dictmodels.kanji.KanjiDictionary
import com.github.reline.jisho.linguist.isCJK
import com.github.reline.jisho.requireFile
import com.github.reline.jisho.sql.JishoDatabase
import com.tickaroo.tikxml.TikXml
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.runBlocking
import okio.IOException
import okio.buffer
import okio.source
import java.io.File

suspend fun JishoDatabase.populate(
    dictionaries: List<Dictionary> = emptyList(),
    kanji: Collection<File> = emptyList(),
    radk: Collection<File> = emptyList(),
    krad: Collection<File> = emptyList(),
) = coroutineScope {
    populateKanji(kanji)
    associateEntriesWithKanji(dictionaries)
    populateRadicals(radk, krad)
}

private suspend fun JishoDatabase.populateKanji(dicts: Collection<File>) = coroutineScope {
    dicts.map { file ->
        ensureActive()
        requireFile(file)
        extractKanji(file)
    }.forEach { dictionary ->
        ensureActive()
        insertKanji(dictionary)
    }
}

@Throws(IOException::class)
private fun extractKanji(file: File): KanjiDictionary {
    logger.debug("Extracting kanji from ${file.name}...")
    try {
        file.inputStream().source().buffer().use { source ->
            return TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
                .read(source, KanjiDictionary::class.java)
        }
    } catch (e: IOException) {
        throw IOException("Failed to read ${file.name}", e)
    }
}

private suspend fun JishoDatabase.insertKanji(dictionary: KanjiDictionary) = coroutineScope {
    logger.debug("Inserting kanji characters...")
    transaction {
        dictionary.characters?.forEach {
            ensureActive()
            kanjiRadicalQueries.insertKanji(it.literal, it.strokeCount.toLong())
        }
    }
    logger.debug("Inserted ${dictionary.characters?.size ?: 0} kanji characters")
}

@Throws(IOException::class)
private fun JishoDatabase.populateRadicals(
    radk: Collection<File>,
    krad: Collection<File>,
) = runBlocking {
    radk.map { file ->
        check(file.exists()) { "$file does not exist" }
        ensureActive()
        file.source().buffer().use { decodeRadicals(it) }
    }.forEach {
        ensureActive()
        insertRadk(it)
    }

    // todo: insert krad
}

suspend fun JishoDatabase.insertRadk(radicals: List<Radk>) = coroutineScope {
    logger.debug("Inserting radicals...")
    val kanji = transactionWithResult(noEnclosing = true) {
        radicals.flatMap { radical ->
            ensureActive()
            // todo: upsert to improve performance
            kanjiRadicalQueries.insertRadical(radical.radical.toString(), radical.strokes.toLong())
            val radicalId = utilQueries.lastInsertRowId().executeAsOne()
            radical.kanji.map { radicalId to it }
        }
    }
    logger.debug("Inserted ${radicals.size} radicals")

    logger.debug("Associating radicals with kanji...")
    transaction {
        kanji.forEach { (radicalId, kanji) ->
            ensureActive()
            val kanjiId = kanjiRadicalQueries.selectKanji(kanji.toString()).executeAsOneOrNull()?.id
            kanjiId?.let { kanjiRadicalQueries.insertKanjiRadicalTag(kanjiId, radicalId) }
        }
    }
    logger.debug("Inserted ${kanji.size} records")
}

private suspend fun JishoDatabase.associateEntriesWithKanji(
    dictionaries: List<Dictionary>,
) = coroutineScope {
    val allEntries = dictionaries.flatMap { it.entries }
    logger.debug("Associating entries with kanji... (${allEntries.count()})")
    allEntries.chunked(500_000).forEach { entries ->
        logger.debug("compiling kanji...")
        transaction(noEnclosing = true) {
            entries.forEach { entry ->
                entry.kanji?.forEach { word ->
                    word.value.filter { char -> isCJK(char.code) }.forEach { kanji ->
                        ensureActive()
                        try {
                            val kanjiId = kanjiRadicalQueries.selectKanji(kanji.toString()).executeAsOne().id
                            entryKanjiQueries.insert(entry.id, kanjiId)
                        } catch (e: NullPointerException) {
                            // fixme: characters are printed as ?
                            // fixme: gradle task log is vague
                            logger.warn("$kanji couldn't be found, used in ${word.value}")
                        }
                    }
                }
            }
            logger.debug("Inserted records")
        }
    }
}
