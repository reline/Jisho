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

class KanjiPopulator(private val database: JishoDatabase) {

    fun populate(dictionaries: List<Dictionary>, kanji: Array<File>, radk: Array<File>, krad: Array<File>) {
        populateKanji(kanji)
        associateEntriesWithKanji(dictionaries)
        populateRadicals(radk, krad)
    }

    private fun populateKanji(dicts: Array<File>) {
        dicts.forEach { file ->
            if (!file.exists()) return@forEach
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

    private fun insertKanji(dictionary: KanjiDictionary) = with(database) {
        transaction {
            dictionary.characters?.forEach {
                kanjiRadicalQueries.insertKanji(it.literal, it.strokeCount.toLong())
            }
        }
    }

    private fun populateRadicals(radk: Array<File>, krad: Array<File>) = runBlocking {
        extractRadKFiles(radk)
    }

    private fun extractRadKFiles(files: Array<File>) {
        val radkparser = RadKParser()
        files.forEach { file ->
            if (!file.exists()) return@forEach
            val radicals = try {
                radkparser.parse(file)
            } catch (e: IOException) {
                throw RuntimeException("Failed to read ${file.name}", e)
            }
            insertRadk(radicals)
        }
    }

    private fun insertRadk(radicals: List<Radical>) = with(database) {
        transaction(noEnclosing = true) {
            radicals.forEach { radical ->
                kanjiRadicalQueries.insertRadical(radical.value.toString(), radical.strokes.toLong())
                // fixme: should be unique, can probably speed up a little here using lastInsertRowId()
                val radicalId = kanjiRadicalQueries.selectRadical(radical.value.toString()).executeAsOne().id
                radical.kanji.forEach { kanji ->
                    val kanjiId = kanjiRadicalQueries.selectKanji(kanji.toString()).executeAsOne().id
                    kanjiRadicalQueries.insertKanjiRadicalTag(kanjiId, radicalId)
                }
            }
        }
    }

    private fun associateEntriesWithKanji(dictionaries: List<Dictionary>) = with(database) {
        transaction(noEnclosing = true) {
            dictionaries.forEach { dictionary ->
                dictionary.entries.forEach { entry ->
                    entry.kanji?.forEach { word ->
                        word.value.filter { char -> isCJK(char.toInt()) }.forEach { kanji ->
                            try {
                                val kanjiId = kanjiRadicalQueries.selectKanji(kanji.toString()).executeAsOne().id
                                entryKanjiQueries.insert(entry.id, kanjiId)
                            } catch (e: NullPointerException) {
                                println("$kanji couldn't be found, used in ${word.value}")
                            }
                        }
                    }
                }
            }
        }
    }
}

