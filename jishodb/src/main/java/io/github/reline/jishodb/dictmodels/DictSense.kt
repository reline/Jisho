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
import com.tickaroo.tikxml.annotation.Xml
import io.github.reline.jishodb.dbmodels.Sense

/**
 *  The sense element will record the translational equivalent
    of the Japanese word, plus other related information. Where there
    are several distinctly different meanings of the word, multiple
    sense elements will be employed.
 */
@Xml(name = "sense")
open class DictSense {

    @Element
    var kanjiTags: MutableList<KanjiTag>? = null

    @Element
    var readingTags: MutableList<ReadingTag>? = null

    @Element
    var partsOfSpeech: MutableList<PartOfSpeech>? = null

    @Element
    var seeAlso: MutableList<XReference>? = null

    @Element
    var antonyms: MutableList<Antonym>? = null

    @Element
    var fields: MutableList<Field>? = null

    @Element
    var miscellaneous: MutableList<Misc>? = null

    @Element
    var information: MutableList<SenseInfo>? = null

    @Element
    var sources: MutableList<Source>? = null

    @Element
    var dialects: MutableList<Dialect>? = null

    @Element
    var glosses: MutableList<Gloss>? = null

    fun getSense(): Sense {
        return Sense(glosses?.mapTo(ArrayList(), {it.value}) ?: emptyList(),
                miscellaneous?.mapTo(ArrayList(), {it.getTag()}) ?: emptyList(),
                information?.mapTo(ArrayList(), {it.value}) ?: emptyList())
    }
}