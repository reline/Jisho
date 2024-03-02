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
import com.github.reline.jisho.sql.JishoDatabase
import com.tickaroo.tikxml.TikXml
import kotlinx.coroutines.runBlocking
import okio.Buffer
import okio.EOFException
import okio.IOException
import okio.buffer
import okio.source
import java.io.File

class DictionaryPopulator(private val database: JishoDatabase) {

    fun populate(dicts: Array<File>) = runBlocking {
        return@runBlocking dicts.mapNotNull { dict ->
            if (!dict.exists()) return@mapNotNull null
            val dictionary = extractDictionary(dict)
            insertDictionary(dictionary)
            return@mapNotNull dictionary
        }
    }

    private fun extractDictionary(file: File): Dictionary {
        try {
            file.inputStream().source().buffer().use {
                return TikXml.Builder()
                    .exceptionOnUnreadXml(false)
                    .build()
                    .read(it, Dictionary::class.java)
            }
        } catch (e: IOException) {
            throw Exception("Failed to read ${file.name}", e)
        }
    }

    private fun insertEntries(entries: List<Entry>) = with(database) {
        transaction {
            entries.forEach { entry ->
                entryQueries.insert(entry.id, entry.isCommon(), entry.kanji?.firstOrNull()?.value, entry.readings.first().value)
                entry.kanji?.forEach { kanji ->
                    japaneseQueries.insert(entry.id, kanji.value)
                }
                entry.readings.forEach { reading ->
                    readingQueries.insert(entry.id, reading.value)
                }
            }
        }
    }

    private fun insertPartsOfSpeech(entries: List<Entry>) = with(database) {
        transaction {
            entries.forEach { entry ->
                entry.senses.forEach { sense ->
                    sense.partsOfSpeech?.forEach { pos ->
                        partOfSpeechQueries.insert(pos.decoded())
                    }
                }
            }
        }
    }

    private fun insertSenses(entries: List<Entry>) = with(database) {
        val list = arrayListOf<Long>()

        transaction {
            entries.forEach { entry ->
                entry.senses.forEach { sense ->
                    senseQueries.insert(entry.id)
                    val senseId = utilQueries.lastInsertRowId().executeAsOne()
                    sense.glosses?.forEach { gloss ->
                        glossQueries.insert(senseId, gloss.value)
                    }
                    list.add(senseId)
                }
            }
        }

        val iter = list.iterator()

        transaction {
            entries.forEach { entry ->
                entry.senses.forEach { sense ->
                    val senseId = iter.next()
                    sense.partsOfSpeech?.forEach { pos ->
                        sensePosTagQueries.insert(
                                senseId,
                                partOfSpeechQueries.selectPosIdWhereValueEquals(pos.decoded())
                                        .executeAsOne()
                        )
                    }
                }
            }
        }
    }

    private fun insertDictionary(dictionary: Dictionary) = with(database) {
        val entries = dictionary.entries

        insertEntries(entries)
        insertPartsOfSpeech(entries)
        insertSenses(entries)
    }
}