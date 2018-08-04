/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

/**
 * Within each sense will be one or more "glosses", i.e.
target-language words or phrases which are equivalents to the
Japanese word. This element would normally be present, however it
may be omitted in entries which are purely for a cross-reference.
 */
@Xml(name = "gloss")
class Gloss {

    @TextContent
    lateinit var value: String

    /**
     *  Defines the target language of the
        gloss. It will be coded using the three-letter language code from
        the ISO 639 standard. When absent, the value "eng" (i.e. English)
        is the default value.
     */
    @Attribute(name = "xml:lang")
    var language = "eng"

    /**
     *  Defines the gender of the gloss (typically
        a noun in the target language. When absent, the gender is either
        not relevant or has yet to be provided.
     */
    @Attribute(name = "g_gend")
    var gender: String? = null

}