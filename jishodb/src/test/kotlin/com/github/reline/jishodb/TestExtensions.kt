package com.github.reline.jishodb

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