/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import com.squareup.sqldelight.db.SqlDriver

fun SqlDriver.hasDuplicateValues(tableName: String, columnName: String): Boolean {
    executeQuery(
            null,
            "SELECT $columnName, COUNT(*) c FROM $tableName GROUP BY $columnName HAVING c > 1;",
            0
    ).use { cursor ->
        return cursor.next()
    }
}