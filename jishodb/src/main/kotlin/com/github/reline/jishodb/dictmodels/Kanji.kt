/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import java.lang.StringBuilder

/**
 * The kanji element, or in its absence, the reading element, is
 * the defining component of each entry.
 * The overwhelming majority of entries will have a single kanji
 * element associated with a word in Japanese. Where there are
 * multiple kanji elements within an entry, they will be orthographical
 * variants of the same word, either using variations in okurigana, or
 * alternative and equivalent kanji. Common "mis-spellings" may be
 * included, provided they are associated with appropriate information
 * fields. Synonyms are not included; they may be indicated in the
 * cross-reference field associated with the sense element.
 */
@Xml(name = "k_ele")
open class Kanji {

    /**
     * This element will contain a word or short phrase in Japanese
     * which is written using at least one non-kana character (usually kanji,
     * but can be other characters). The valid characters are
     * kanji, kana, related characters such as chouon and kurikaeshi, and
     * in exceptional cases, letters from other alphabets.
     */
    @PropertyElement(name = "keb")
    lateinit var value: String

    @Element
    var information: MutableList<KanjiInfo>? = null

    @Element
    var priorities: MutableList<KanjiPriority>? = null

    val statement: StringBuilder
        get() {
            val builder = StringBuilder("CREATE TABLE IF NOT EXISTS Kanji(value TEXT NOT NULL PRIMARY KEY)")
                    .append("INSERT INTO Kanji (value) VALUES $value")
            // TODO: write bridge tables
//            information?.forEach { builder.append(it.statement) }
//            priorities?.forEach { builder.append(it.statement) }
            return builder
        }

    fun isCommon(): Boolean {
        priorities?.forEach {
            if (it.isCommon()) {
                return true
            }
        }
        return false
    }
}