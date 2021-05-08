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
import com.github.reline.jisho.logger
import com.github.reline.jisho.skipBom
import com.github.reline.jisho.sql.JishoDatabase
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okio.Buffer
import java.io.File
import java.lang.reflect.Type

class OkuriganaPopulator(private val database: JishoDatabase) {

    fun populate(dictionaries: List<Dictionary>, files: Array<File>) {
        dictionaries.forEachIndexed { i, dictionary ->
            val file = files[i]
            logger.info("Extracting okurigana from ${file.name}...")
            val okurigana = extractOkurigana(file)
            logger.info("Inserting okurigana...")
            insertOkurigana(dictionary, okurigana)
            logger.info("✓ ${file.name}")
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

        val inputStream = file.inputStream()

        val source = Buffer().readFrom(inputStream)
        source.skipBom()

        val parseStart = System.currentTimeMillis()

        logger.info("Parsing ${file.name}...")

        val entries = try {
            adapter.fromJson(source) ?: throw IllegalStateException("Entries were null")
        } finally {
            source.clear()
            inputStream.close()
        }

        val parseEnd = System.currentTimeMillis()

        val map = OkuriganaEntries()
        entries.forEach {
            map.addOkurigana(it)
        }

        logger.info("${file.name}: Parsing ${entries.size} okurigana took ${(parseEnd - parseStart)}ms")
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