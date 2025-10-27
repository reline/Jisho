///*
// * Copyright 2020 Nathaniel Reline
// *
// * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
// * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
// * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
// */
//
//package com.github.reline.jisho.dictmodels.jmdict
//
//import kotlinx.serialization.Serializable
//
///**
// * Entries consist of kanji elements, reading elements,
// * general information and sense elements. Each entry must have at
// * least one reading element and one sense element. Others are optional.
// */
////@Xml(name = "entry")
//@Serializable
//open class Entry {
//
//    /** A unique numeric sequence number for each entry **/
////    @PropertyElement(name = "ent_seq")
//    var id: Long = 0
//
////    @Element
//    var kanji: MutableList<Kanji>? = null
//
////    @Element
//    lateinit var readings: MutableList<Reading>
//
////    @Element
//    var senses: MutableList<DictSense> = mutableListOf()
//
////    @Element
////    var translations: MutableList<Translation>? = null
//
//    fun isCommon(): Boolean {
//        readings.forEach {
//            if (it.isCommon()) {
//                return true
//            }
//        }
//        kanji?.forEach {
//            if (it.isCommon()) {
//                return true
//            }
//        }
//        return false
//    }
//
//}