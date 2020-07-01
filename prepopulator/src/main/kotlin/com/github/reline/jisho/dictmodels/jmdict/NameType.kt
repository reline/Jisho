/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.dictmodels.jmdict

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "name_type")
class NameType {

    @TextContent
    lateinit var value: String

    fun get(): String {
        val t = value.removePrefix("&").removeSuffix(";")
        return TYPES[t] ?: ""
    }

    companion object {
        // TODO: this could be it's own static table, with primary keys for each row to enable faster lookups
        private val TYPES = hashMapOf(
                "surname" to "family or surname",
                "place" to "place name",
                "unclass" to "unclassified name",
                "company" to "company name",
                "product" to "product name",
                "work" to "work of art, literature, music, etc. name",
                "masc" to "male given name or forename",
                "fem" to "female given name or forename",
                "person" to "full name of a particular person",
                "given" to "given name or forename, gender not specified",
                "station" to "railway station",
                "organization" to "organization name",
                "ok" to "old or irregular kana form"
        )
    }

}
