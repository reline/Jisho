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
import com.github.reline.jisho.dictmodels.okurigana.OkuriganaEntry
import com.github.reline.jisho.skipBom
import com.github.reline.jisho.sql.JishoDatabase
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tickaroo.tikxml.TikXml
import kotlinx.coroutines.runBlocking
import okio.IOException
import okio.buffer
import okio.source
import java.io.File
import java.lang.reflect.Type

class DictionaryPopulator(private val database: JishoDatabase) {
    fun populate(dictionaryFile: File, okuriganaFile: File): Dictionary {
        requireFile(dictionaryFile)
        requireFile(okuriganaFile)
        val dictionary = extractDictionary(dictionaryFile)
        insertDictionary(dictionary)
        insertOkurigana(dictionary, extractOkurigana(okuriganaFile))
        return dictionary
    }

    private fun requireFile(file: File) {
        require(file.exists()) { "file does not exist: $file" }
        require(file.isFile) { "not a file: $file" }
    }

    @Deprecated(
        "Populate with okurigana instead",
        ReplaceWith("this.populate(dictionaryFile, okuriganaFile)")
    )
    fun populate(dicts: Collection<File>) = runBlocking {
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

    /**
     * See okurigana.json
     */
    private fun extractOkurigana(file: File): OkuriganaEntries {
        val moshi = Moshi.Builder()
            .build()
        val type: Type = Types.newParameterizedType(List::class.java, OkuriganaEntry::class.java)
        val adapter: JsonAdapter<List<OkuriganaEntry>> = moshi.adapter(type)

        val map = OkuriganaEntries()
        try {
            file.inputStream().source().buffer().use { source ->
                source.skipBom()
                val entries = adapter.fromJson(source) ?: throw IllegalStateException("Entries were null")
                entries.forEach {
                    map.addOkurigana(it)
                }
                println("Finished reading ${file.name}")
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to read ${file.name}", e)
        }
        return map
    }

    private fun insertOkurigana(dictionary: Dictionary, furigana: OkuriganaEntries) = with(database) {
        transaction {
            dictionary.entries.forEach { entry ->
                val match = furigana.getOkurigana(entry) ?: return@forEach
                match.furigana.forEach { okurigana ->
                    rubyQueries.insert(entry.id, okurigana.ruby, okurigana.rt)
                }
            }
        }
    }
}

typealias OkuriganaEntries = HashMap<String, OkuriganaEntry>
fun OkuriganaEntries.getOkurigana(entry: Entry): OkuriganaEntry? {
    return get(entry.readings.firstOrNull()?.value + entry.kanji?.firstOrNull()?.value)
}
fun OkuriganaEntries.addOkurigana(entry: OkuriganaEntry) {
    put(entry.reading+entry.text, entry)
}
