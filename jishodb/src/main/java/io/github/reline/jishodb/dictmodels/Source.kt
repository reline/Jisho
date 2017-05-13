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
 * This element records the information about the source
language(s) of a loan-word/gairaigo. If the source language is other
than English, the language is indicated by the xml:lang attribute.
The element value (if any) is the source word or phrase.
 */
@Xml(name = "lsource")
class Source {

    @TextContent
    lateinit var value: String

    /**
     * Defines the language(s) from which a loanword is drawn.  It will
     * be coded using the three-letter language code from the ISO 639-2 standard.
     * When absent, the value "eng" (i.e. English) is the default value.
     * The bibliographic (B) codes are used.
     */
    @Attribute(name = "xml:lang")
    var language = "eng"

    /**
     * Indicates whether the lsource element
    fully or partially describes the source word or phrase of the
    loanword. If absent, it will have the implied value of "full".
    Otherwise it will contain "part".
     */
    @Attribute(name = "ls_type")
    var type = "full"

    /**
     * The ls_wasei attribute indicates that the Japanese word
    has been constructed from words in the source language, and
    not from an actual phrase in that language. Most commonly used to
    indicate "waseieigo".
     */
    @Attribute(name = "la_wasei")
    var waseieigo = "n" // n for false, y for true

}