/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.dictmodels.kanji

//@Xml
open class Radical {

//    @Element
    lateinit var values: MutableList<Value>

//    @Xml(name = "rad_value")
    open class Value {
        /**
         * The rad_type attribute states the type of radical classification.
         * classical - as recorded in the KangXi Zidian.
         * nelson_c - as used in the Nelson "Modern Japanese-English
         * Character Dictionary" (i.e. the Classic, not the New Nelson).
         * This will only be used where Nelson reclassified the kanji.
         */
//        @Attribute(name = "rad_type")
        lateinit var type: String

        /**
         * The radical number, in the range 1 to 214. The particular
         * classification type is stated in the rad_type attribute.
         */
//        @TextContent
        lateinit var value: String
    }
}