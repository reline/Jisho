/*
 * Copyright 2017 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

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

    fun isCommon(): Boolean {
        priorities?.forEach {
            if (it.isCommon()) {
                return true
            }
        }
        return false
    }
}