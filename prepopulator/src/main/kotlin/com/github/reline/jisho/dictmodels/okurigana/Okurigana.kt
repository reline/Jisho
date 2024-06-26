/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.dictmodels.okurigana

import kotlinx.serialization.Serializable

@Serializable
data class OkuriganaEntry(
    val text: String,
    val reading: String,
    val furigana: List<Okurigana>
)

@Serializable
data class Okurigana(
    val ruby: String,
    val rt: String? = null
)
