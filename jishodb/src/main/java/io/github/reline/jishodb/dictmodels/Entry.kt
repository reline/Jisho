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
import io.github.reline.jishodb.dbmodels.Japanese
import io.github.reline.jishodb.dbmodels.Sense
import io.realm.RealmList

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

    fun getJapanese() : RealmList<Japanese> {
        val japanese = RealmList<Japanese>()
        readings.forEach {
            val reading = it
            if (kanji?.isEmpty() ?: true) {
                japanese.add(Japanese(reading = reading.value))
            } else {
                kanji!!.forEach {
                    japanese.add(Japanese(it.value, reading.value))
                }
            }
        }
        return japanese
    }

    fun getSenses() = senses.mapTo(RealmList<Sense>()){ it.getSense() }

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