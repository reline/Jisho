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

    fun isCommon(): Boolean {
        priorities?.forEach {
            if (it.isCommon()) {
                return true
            }
        }
        return false
    }
}