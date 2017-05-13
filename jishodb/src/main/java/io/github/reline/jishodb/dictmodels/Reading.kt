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

@Xml(name = "r_ele")
open class Reading {

    @PropertyElement(name = "reb")
    var value: String? = null

    @PropertyElement(name = "re_nokanji")
    var isNotTrueReading: Boolean? = null

    @Element
    lateinit var restrictions: MutableList<ReadingRestriction>

    @Element
    lateinit var information: MutableList<ReadingInfo>

    @Element
    lateinit var priorities: MutableList<ReadingPriority>
}