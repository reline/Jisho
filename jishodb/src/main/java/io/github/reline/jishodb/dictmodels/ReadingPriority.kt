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
 *  This and the equivalent ke_pri field are provided to record
information about the relative priority of the entry,  and consist
of codes indicating the word appears in various references which
can be taken as an indication of the frequency with which the word
is used. This field is intended for use either by applications which
want to concentrate on entries of  a particular priority, or to
generate subset files.
The current values in this field are:
- news1/2: appears in the "wordfreq" file compiled by Alexandre Girardi
from the Mainichi Shimbun. (See the Monash ftp archive for a copy.)
Words in the first 12,000 in that file are marked "news1" and words
in the second 12,000 are marked "news2".
- ichi1/2: appears in the "Ichimango goi bunruishuu", Senmon Kyouiku
Publishing, Tokyo, 1998.  (The entries marked "ichi2" were
demoted from ichi1 because they were observed to have low
frequencies in the WWW and newspapers.)
- spec1 and spec2: a small number of words use this marker when they
are detected as being common, but are not included in other lists.
- gai1/2: common loanwords, based on the wordfreq file.
- nfxx: this is an indicator of frequency-of-use ranking in the
wordfreq file. "xx" is the number of the set of 500 words in which
the entry can be found, with "01" assigned to the first 500, "02"
to the second, and so on. (The entries with news1, ichi1, spec1, spec2
and gai1 values are marked with a "(P)" in the EDICT and EDICT2
files.)

The reason both the kanji and reading elements are tagged is because
on occasions a priority is only associated with a particular
kanji/reading pair.
 */
@Xml(name = "re_pri")
open class ReadingPriority {

    @TextContent
    lateinit var value: String

    fun isCommon() = Priority.isCommon(value)
}