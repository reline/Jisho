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
 * Part-of-speech information about the entry/sense. Should use
appropriate entity codes. In general where there are multiple senses
in an entry, the part-of-speech of an earlier sense will apply to
later senses unless there is a new part-of-speech indicated.
 */
@Xml(name = "pos")
class PartOfSpeech {

    @TextContent
    lateinit var value: String
}