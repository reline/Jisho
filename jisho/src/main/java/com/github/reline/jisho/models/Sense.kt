/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sense(
    @field:Json(name = "english_definitions")
    val englishDefinitions: List<String>,
    @field:Json(name = "parts_of_speech")
    val partsOfSpeech: List<String>,
    val links: List<Link>,
    @field:Json(name = "see_also")
    val seeAlso: List<String>
//        val tags: ArrayList<String>,
//        val restrictions: ArrayList<String>,
//        val antonyms: ArrayList<String>,
//        val source: ArrayList<Source>,
//        val info: ArrayList<String>
) : Parcelable
