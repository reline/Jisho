/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jishodb.dictmodels.jmdict

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

/**
 *  This and the equivalent re_pri field are provided to record
    information about the relative priority of the entry,  and consist
    of codes indicating the word appears in various references which
    can be taken as an indication of the frequency with which the word
    is used. This field is intended for use either by applications which
    want to concentrate on entries of  a particular priority, or to
    generate subset files.
    The current values in this field are: {@link Priority}


    The reason both the kanji and reading elements are tagged is because
    on occasions a priority is only associated with a particular
    kanji/reading pair.
 */
@Xml(name = "ke_pri")
open class KanjiPriority {

    @TextContent
    lateinit var value: String

    fun isCommon() = Priority.isCommon(value)
}