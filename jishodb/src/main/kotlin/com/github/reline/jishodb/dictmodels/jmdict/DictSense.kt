/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jishodb.dictmodels.jmdict

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

/**
 *  The sense element will record the translational equivalent
    of the Japanese word, plus other related information. Where there
    are several distinctly different meanings of the word, multiple
    sense elements will be employed.
 */
@Xml(name = "sense")
open class DictSense {

//    @Element
//    var kanjiTags: MutableList<KanjiTag>? = null

//    @Element
//    var readingTags: MutableList<ReadingTag>? = null

    @Element
    var partsOfSpeech: MutableList<PartOfSpeech>? = null

//    @Element
//    var seeAlso: MutableList<XReference>? = null

//    @Element
//    var antonyms: MutableList<Antonym>? = null

//    @Element
//    var fields: MutableList<Field>? = null

//    @Element
//    var miscellaneous: MutableList<Misc>? = null

//    @Element
//    var information: MutableList<SenseInfo>? = null

//    @Element
//    var sources: MutableList<Source>? = null

//    @Element
//    var dialects: MutableList<Dialect>? = null

    @Element
    var glosses: MutableList<Gloss>? = null

//    fun getSense(): Sense {
//        return Sense(glosses?.mapTo(ArrayList(), {it.value}) ?: emptyList(),
//                miscellaneous?.mapTo(ArrayList(), {it.getTag()}) ?: emptyList(),
//                information?.mapTo(ArrayList(), {it.value}) ?: emptyList())
//    }
}