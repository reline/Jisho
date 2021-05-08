/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.parsers

import com.github.reline.jisho.dictmodels.Radical
import java.io.File

class RadKParser {
    fun parse(file: File): List<Radical> {
        val radk = ArrayList<Radical>()
        var currentRadical: Char? = null
        var strokes = 0
        var kanji = ArrayList<Char>()
        file.forEachLine loop@{
            if (it.startsWith('#') || it.isBlank()) {
                return@loop
            }

            if (it.startsWith('$')) {
                // new radical, save the previous one
                currentRadical?.let { radical ->
                    radk.add(Radical(radical, strokes, kanji))
                    kanji = ArrayList()
                    strokes = 0
                }

                // new radical
                currentRadical = it[2]
                val start = nextNonWhitespace(3, it)
                val end = nextWhitespace(start, it)
                strokes = Integer.parseInt(it.substring(start, end))
            } else {
                // line with just kanji on it
                kanji.addAll(it.toList())
            }
        }

        // save the last radical. I know, nasty.
        currentRadical?.let { radical ->
            radk.add(Radical(radical, strokes, kanji))
        }

        return radk
    }

    private fun nextNonWhitespace(startIndex: Int, s: String): Int {
        if (s.length <= startIndex) {
            throw IllegalArgumentException("No non-whitespace character after $startIndex in $s")
        }
        val c = s[startIndex]
        return if (c == '\n' || c == ' ' || c == '\r' || c == '\t') {
            nextNonWhitespace(startIndex + 1, s)
        } else {
            startIndex
        }
    }

    private fun nextWhitespace(startIndex: Int, s: String): Int {
        if (s.length <= startIndex) {
            return startIndex
        }
        val c = s[startIndex]
        return if (c == '\n' || c == ' ' || c == '\r' || c == '\t') {
            startIndex
        } else {
            nextWhitespace(startIndex + 1, s)
        }
    }
}
