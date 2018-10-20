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
 *  The reading element typically contains the valid readings
    of the word(s) in the kanji element using modern kanadzukai.
    Where there are multiple reading elements, they will typically be
    alternative readings of the kanji element. In the absence of a
    kanji element, i.e. in the case of a word or phrase written
    entirely in kana, these elements will define the entry.
 */
@Xml(name = "r_ele")
open class Reading {

    /**
     *  this element content is restricted to kana and related
        characters such as chouon and kurikaeshi. Kana usage will be
        consistent between the keb and reb elements; e.g. if the keb
        contains katakana, so too will the reb.
     */
    @PropertyElement(name = "reb")
    lateinit var value: String

    /**
     *  This element, which will usually have a null value, indicates
        that the reb, while associated with the keb, cannot be regarded
        as a true reading of the kanji. It is typically used for words
        such as foreign place names, gairaigo which can be in kanji or
        katakana, etc.
     */
    @PropertyElement(name = "re_nokanji")
    var isNotTrueReading: Boolean? = null

    @Element
    var restrictions: MutableList<ReadingRestriction>? = null

    @Element
    var information: MutableList<ReadingInfo>? = null

    @Element
    var priorities: MutableList<ReadingPriority>? = null

    val statement: String
        get() {
            val builder = StringBuilder()
            // TODO: insert value and reading
            restrictions?.forEach { builder.append(it.statement) }
            information?.forEach { builder.append(it.statement) }
            priorities?.forEach { builder.append(it.statement) }
            return builder.toString()
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