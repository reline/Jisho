/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

/**
 * This element is used for other relevant information about
the entry/sense. As with part-of-speech, information will usually
apply to several senses.
 */
@Xml(name = "misc")
class Misc {

    @TextContent
    lateinit var value: String

    fun getTag() = Info.get(value)
}