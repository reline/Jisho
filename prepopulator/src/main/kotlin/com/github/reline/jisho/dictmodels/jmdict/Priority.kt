/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.dictmodels.jmdict

/**
 * - news1/2: appears in the "wordfreq" file compiled by Alexandre Girardi
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
 */
object Priority {
    private const val NEWS1 = "news1"
    private const val NEWS2 = "news2"
    private const val ICHI1 = "ichi1"
    private const val ICHI2 = "ichi2"
    private const val SPEC1 = "spec1"
    private const val SPEC2 = "spec2"
    private const val GAI1 = "gai1"
    private val priorites = listOf(NEWS1, NEWS2, ICHI1, ICHI2, SPEC1, SPEC2, GAI1)
    private const val NFXX = """nf\+?\d+"""

    fun isCommon(priority: String): Boolean {
        return priorites.contains(priority) || priority.matches(Regex(NFXX))
    }
}