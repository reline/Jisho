/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.dictmodels.kanji

/**
 * The cp_value contains the codepoint of the character in a particular
 * standard. The standard will be identified in the cp_type attribute.
 */
//@Xml
open class CodePoint{
//    @Element
    lateinit var codings: MutableList<Coding>

//    @Xml(name = "cp_value")
    open class Coding {
        /**
         * The cp_type attribute states the coding standard applying to the
         * element. The values assigned so far are:
         * jis208 - JIS X 0208-1997 - kuten coding (nn-nn)
         * jis212 - JIS X 0212-1990 - kuten coding (nn-nn)
         * jis213 - JIS X 0213-2000 - kuten coding (p-nn-nn)
         * ucs - Unicode 4.0 - hex coding (4 or 5 hexadecimal digits)
         */
//        @Attribute(name = "cp_type")
        lateinit var type: String

//        @TextContent
        lateinit var value: String
    }
}