/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import com.github.reline.jisho.dictmodels.kanji.KanjiDictionary
import com.tickaroo.tikxml.TikXml
import okio.Buffer
import java.io.File

fun main() = KanjiPopulator.run()

object KanjiPopulator {
    fun run() {
        println("Extracting kanji...")
        arrayOf(
                File("$buildDir/dict/kanjidic2.xml")
        ).forEach { file ->
            val dictionary = extractKanji(file)
            insertKanji(dictionary)
        }
    }

    private fun extractKanji(file: File): KanjiDictionary {

        val inputStream = file.inputStream()
        val source = Buffer().readFrom(inputStream)

        val parseStart = System.currentTimeMillis()

        val dictionary: KanjiDictionary = TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
                .read(source, KanjiDictionary::class.java)
        source.clear()
        inputStream.close()

        val parseEnd = System.currentTimeMillis()

        println("${file.name}: Parsing ${dictionary.characters?.size} kanji took ${(parseEnd - parseStart)}ms")

        return dictionary
    }

    private fun insertKanji(dictionary: KanjiDictionary) = with(database) {
        transaction {
            dictionary.characters?.forEach {
//            kanjiRadicalQueries.insertKanji(it.literal)
            }
        }
    }
}

