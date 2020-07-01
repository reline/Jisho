/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.dictmodels.jmdict

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

/**
 * This element is used to indicate when the reading only applies
to a subset of the keb elements in the entry. In its absence, all
readings apply to all kanji elements. The contents of this element
must exactly match those of one of the keb elements.
 */
@Xml(name = "re_restr")
open class ReadingRestriction {

    @TextContent
    lateinit var kanji: String

}