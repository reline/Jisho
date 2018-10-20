package com.github.reline.jishodb

import java.io.File

class KRadParser {

    /**
     * @return A map of kanji to radicals that make up that kanji
     */
    fun parse(file: File): Map<Char, List<Char>> {
        val krad = HashMap<Char, List<Char>>()
        file.forEachLine loop@{
            if (it.startsWith('#') || it.isBlank()) {
                return@loop
            }

            val parts = it.split(' ')
            // first character is the kanji
            val kanji = parts[0][0]
            val radicals = ArrayList<Char>()
            // second character is the :, skip it. the rest are radicals.
            for (i in 2 until parts.size) {
                radicals.add(parts[i][0])
            }
            krad[kanji] = radicals
        }
        return krad
    }
}
