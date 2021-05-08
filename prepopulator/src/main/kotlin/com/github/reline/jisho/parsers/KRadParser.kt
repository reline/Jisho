/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.parsers

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
