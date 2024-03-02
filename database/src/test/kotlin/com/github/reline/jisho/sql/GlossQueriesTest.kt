/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.sql

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.Test
import kotlin.properties.Delegates
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GlossQueriesTest {

    private var senseId by Delegates.notNull<Long>()
    private lateinit var database: JishoDatabase

    @BeforeTest
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insert(1, false, "今日は", "こんにちは")
                japaneseQueries.insert(1, "今日は")
                readingQueries.insert(1, "こんにちは")
                senseQueries.insert(1)
                senseId = utilQueries.lastInsertRowId().executeAsOne()
                glossQueries.insert(senseId, "hello")
                glossQueries.insert(senseId, "good day (daytime greeting)")
            }
        }
    }

    @Test
    fun testInsertAllowDuplicateGloss() = with(database) {
        transaction {
            val expected = glossQueries.selectGlossIdWhereValueEquals("hello").executeAsList()
            assertEquals(1, expected.size)
            glossQueries.insert(senseId, "hello")
            val actual = glossQueries.selectGlossIdWhereValueEquals("hello").executeAsList()
            assertEquals(2, actual.size)
        }
    }

    @Test
    fun test() = with(database) {
        val glosses = glossQueries.selectGlossWhereSenseIdEquals(senseId).executeAsList()
        assertTrue {
            glosses.toTypedArray().contentEquals(
                    arrayOf("hello", "good day (daytime greeting)")
            )
        }
    }
}