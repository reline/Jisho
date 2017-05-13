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

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

/**
 * Within each sense will be one or more "glosses", i.e.
target-language words or phrases which are equivalents to the
Japanese word. This element would normally be present, however it
may be omitted in entries which are purely for a cross-reference.
 */
@Xml(name = "gloss")
class Gloss {

    @TextContent
    lateinit var value: String

    /**
     *  Defines the target language of the
        gloss. It will be coded using the three-letter language code from
        the ISO 639 standard. When absent, the value "eng" (i.e. English)
        is the default value.
     */
    @Attribute(name = "xml:lang")
    var language = "eng"

    /**
     *  Defines the gender of the gloss (typically
        a noun in the target language. When absent, the gender is either
        not relevant or has yet to be provided.
     */
    @Attribute(name = "g_gend")
    var gender: String? = null

}