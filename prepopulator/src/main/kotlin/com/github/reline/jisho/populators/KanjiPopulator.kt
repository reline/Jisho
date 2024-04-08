/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.populators

import com.github.reline.jisho.parsers.RadKParser
import com.github.reline.jisho.dictmodels.Radical
import com.github.reline.jisho.dictmodels.jmdict.Dictionary
import com.github.reline.jisho.dictmodels.kanji.KanjiDictionary
import com.github.reline.jisho.linguist.isCJK
import com.github.reline.jisho.sql.JishoDatabase
import com.tickaroo.tikxml.TikXml
import kotlinx.coroutines.runBlocking
import okio.IOException
import okio.buffer
import okio.source
import java.io.File

fun JishoDatabase.populate(
    dictionaries: List<Dictionary>,
    kanji: Collection<File>,
    radk: Collection<File>,
    krad: Collection<File>,
) {
    populateKanji(kanji)
    associateEntriesWithKanji(dictionaries)
    populateRadicals(radk, krad)
}

private fun JishoDatabase.populateKanji(dicts: Collection<File>) {
    dicts.forEach { file ->
        check(file.exists()) { "$file does not exist" }
        val dictionary = extractKanji(file)
        insertKanji(dictionary)
    }
}

private fun extractKanji(file: File): KanjiDictionary {
    try {
        file.inputStream().source().buffer().use { source ->
            return TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
                .read(source, KanjiDictionary::class.java)
        }
    } catch (e: IOException) {
        throw RuntimeException("Failed to read ${file.name}", e)
    }
}

private fun JishoDatabase.insertKanji(dictionary: KanjiDictionary) =
    transaction {
        dictionary.characters?.forEach {
            kanjiRadicalQueries.insertKanji(it.literal, it.strokeCount.toLong())
        }
    }


private fun JishoDatabase.populateRadicals(
    radk: Collection<File>,
    krad: Collection<File>,
) = runBlocking {
    extractRadKFiles(radk)
}

private fun JishoDatabase.extractRadKFiles(files: Collection<File>) {
    val radkparser = RadKParser()
    files.forEach { file ->
        check(file.exists()) { "$file does not exist" }
        val radicals = try {
            radkparser.parse(file)
        } catch (e: IOException) {
            throw RuntimeException("Failed to read ${file.name}", e)
        }
        insertRadk(radicals)
    }
}

private fun JishoDatabase.insertRadk(radicals: List<Radical>) =
    transaction(noEnclosing = true) {
        radicals.forEach { radical ->
            kanjiRadicalQueries.insertRadical(radical.value.toString(), radical.strokes.toLong())
            val radicalId = utilQueries.lastInsertRowId().executeAsOne()
            radical.kanji.forEach { kanji ->
                val kanjiId = kanjiRadicalQueries.selectKanji(kanji.toString()).executeAsOne().id
                kanjiRadicalQueries.insertKanjiRadicalTag(kanjiId, radicalId)
            }
        }
    }

private fun JishoDatabase.associateEntriesWithKanji(
    dictionaries: List<Dictionary>,
) = transaction(noEnclosing = true) {
    dictionaries.forEach { dictionary ->
        dictionary.entries.forEach { entry ->
            entry.kanji?.forEach { word ->
                word.value.filter { char -> isCJK(char.code) }.forEach { kanji ->
                    try {
                        val kanjiId = kanjiRadicalQueries.selectKanji(kanji.toString()).executeAsOne().id
                        entryKanjiQueries.insert(entry.id, kanjiId)
                    } catch (e: NullPointerException) {
                        // todo: verbose/warning log
                        println("$kanji couldn't be found, used in ${word.value}")
                    }
                }
            }
        }
    }
}
