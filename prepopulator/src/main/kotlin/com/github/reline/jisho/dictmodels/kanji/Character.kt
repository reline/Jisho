/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.dictmodels.kanji

//@Xml(name = "character")
open class Character {

    /**
     * The character itself in UTF8 coding.
     */
//    @PropertyElement(name = "literal")
    lateinit var literal: String

    /**
     * The codepoint element states the code of the character in the various
     * character set standards.
     */
//    @Element(name = "codepoint")
    lateinit var codepoint: CodePoint
    val codepoints get() = codepoint.codings

//    @Element(name = "radical")
    lateinit var radical: Radical
    val radicals get() = radical.values

//    @Element(name = "misc")
    lateinit var misc: Misc

    val grade get() = misc.grade

    val strokeCounts get() = misc.strokeCounts.map { it.strokeCount.toInt() }
    val strokeCount get() = strokeCounts.first()

    val variants get() = misc.variants ?: emptyList<Misc.Variant>()

    val frequency get() = misc.frequency

    val radicalNames get() = misc.radicalNames?.map { it.value } ?: emptyList()

    val jlpt get() = misc.jlpt

//    @Element(name = "dic_number")
    var dictionaryNumber: DictionaryNumber? = null
    val dictionaryReferences get() = dictionaryNumber?.dictionaryReferences ?: emptyList<DictionaryNumber.DictionaryReference>()

//    @Element(name = "query_code")
    var queryCode: QueryCode? = null
    val queryCodes get() = queryCode?.codes ?: emptyList<QueryCode.Code>()

//    @Element(name = "reading_meaning")
    var readingMeaning: ReadingMeaning? = null
    val readingMeaningGroups get() = readingMeaning?.groups ?: emptyList<ReadingMeaning.Group>()
    val nanori get() = readingMeaning?.nanori ?: emptyList<ReadingMeaning.Nanori>()

}