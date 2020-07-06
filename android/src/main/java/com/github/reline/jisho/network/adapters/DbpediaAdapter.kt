/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.network.adapters

import com.github.reline.jisho.network.qualifiers.Dbpedia
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

internal class DbpediaAdapter {

    @ToJson
    fun toJson(@Dbpedia dbpedia: String) = dbpedia

    @FromJson
    @Dbpedia
    fun fromJson(dbpedia: Any): String {
        return dbpedia as? String ?: false.toString()
    }
}