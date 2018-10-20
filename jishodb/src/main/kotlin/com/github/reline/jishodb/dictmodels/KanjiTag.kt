/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

/**
 * Indicates that the sense is restricted
    to the lexeme represented by the keb.
 */
@Xml(name = "stagk")
class KanjiTag {

    @TextContent
    lateinit var value: String

    val statement: String
        get() {
            return ""
        }
}