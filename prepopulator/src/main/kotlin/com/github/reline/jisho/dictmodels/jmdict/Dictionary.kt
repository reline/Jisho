/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.dictmodels.jmdict

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

/**
 * +	a+	One or more occurrences of a
 * *	a*	Zero or more occurrences of a
 * ?	a?	Either a or nothing
 * ,	a, b	a followed by b
 * |	a | b	a followed by b
 * ()	(expression)	An expression surrounded by parentheses is treated as a unit and could have any one of the following suffixes ?, *, or +.
 */
@Xml(name = "Dictionary")
open class Dictionary {

    @Element
    lateinit var entries: MutableList<Entry>

}