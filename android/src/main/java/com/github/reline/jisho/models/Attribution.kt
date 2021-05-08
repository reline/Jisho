/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.models

import com.github.reline.jisho.network.qualifiers.Dbpedia
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Attribution(
    @field:Json(name = "jmdict")
    val isJmdict: Boolean = false,
    @field:Json(name = "jmnedict")
    val isJmnedict: Boolean = false,
    @field:Dbpedia
    val dbpedia: String = false.toString()
) {
    val isDbpedia: Boolean
        get() = dbpedia != false.toString()
}
