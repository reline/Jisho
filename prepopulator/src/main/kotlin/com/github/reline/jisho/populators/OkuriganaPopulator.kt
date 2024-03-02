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
import okio.IOException
import okio.buffer
import okio.source
import java.io.File
import java.lang.reflect.Type

class OkuriganaPopulator(private val database: JishoDatabase) {

    fun populate(dictionaries: List<Dictionary>, files: Array<File>) {
        dictionaries.forEachIndexed { i, dictionary ->
            val file = files[i]
            if (!file.exists()) return@forEachIndexed
            val okurigana = extractOkurigana(file)
            insertOkurigana(dictionary, okurigana)
        }
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