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
class Attribution(
    @field:Json(name = "jmdict")
    val isJmdict: Boolean,
    @field:Json(name = "jmnedict")
    val isJmnedict: Boolean
    // Issue #14: data is lost when parceled
    // val dbpedia: Any // String or boolean
) : Parcelable
