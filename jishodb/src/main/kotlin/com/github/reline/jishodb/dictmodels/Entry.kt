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

/**
 * Entries consist of kanji elements, reading elements,
 * general information and sense elements. Each entry must have at
 * least one reading element and one sense element. Others are optional.
 */
@Xml(name = "entry")
open class Entry {

    /** A unique numeric sequence number for each entry **/
    @PropertyElement(name = "ent_seq")
    var id: Int = 0

    @Element
    var kanji: MutableList<Kanji>? = null

    @Element
    lateinit var readings: MutableList<Reading>

    @Element
    lateinit var senses: MutableList<DictSense>

//    fun getJapanese() : List<Japanese> {
//        val japanese = arrayListOf<Japanese>()
//        readings.forEach {
//            val reading = it
//            if (kanji?.isEmpty() == true) {
//                japanese.add(Japanese(reading = reading.value))
//            } else {
//                kanji?.forEach {
//                    japanese.add(Japanese(it.value, reading.value))
//                }
//            }
//        }
//        return japanese
//    }

//    fun getSenses(): List<Sense> = senses.mapTo(List<Sense>) { it.getSense() }

    fun isCommon(): Boolean {
        readings.forEach {
            if (it.isCommon()) {
                return true
            }
        }
        kanji?.forEach {
            if (it.isCommon()) {
                return true
            }
        }
        return false
    }

}