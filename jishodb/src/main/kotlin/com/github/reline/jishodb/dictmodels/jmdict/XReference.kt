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
 * This element is used to indicate a cross-reference to another
entry with a similar or related meaning or sense. The content of
this element is typically a keb or reb element in another entry. In some
cases a keb will be followed by a reb and/or a sense number to provide
a precise target for the cross-reference. Where this happens, a JIS
"centre-dot" (0x2126) is placed between the components of the
cross-reference.
 */
@Xml(name = "xref")
class XReference {

    @TextContent
    lateinit var value: String
}