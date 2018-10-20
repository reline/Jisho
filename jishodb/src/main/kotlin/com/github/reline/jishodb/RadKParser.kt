package com.github.reline.jishodb

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
                strokes = Integer.parseInt(it[4].toString())
            } else {
                // line with just kanji on it
                kanji.addAll(it.toList())
            }
        }
        return radk
    }
}
