/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jishodb.dictmodels.jmdict

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
    fun isFullDescription() = type == "full"

    /**
     * The ls_wasei attribute indicates that the Japanese word
    has been constructed from words in the source language, and
    not from an actual phrase in that language. Most commonly used to
    indicate "waseieigo".
     */
    @Attribute(name = "la_wasei")
    var waseieigo = "n" // n for false, y for true
    fun isWaseieigo() = waseieigo == "y"

}