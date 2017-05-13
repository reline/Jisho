/*
 * Copyright 2017 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.reline.jishodb.dictmodels

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