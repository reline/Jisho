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
 * The sense-information elements provided for additional
information to be recorded about a sense. Typical usage would
be to indicate such things as level of currency of a sense, the
regional variations, etc.
 */
@Xml(name = "s_inf")
class SenseInfo {

    @TextContent
    lateinit var value: String
}