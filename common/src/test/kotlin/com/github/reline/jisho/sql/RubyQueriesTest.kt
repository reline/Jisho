/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RubyQueriesTest {

    companion object {
        private const val ENTRY_ID = 1L
    }

    private lateinit var database: JishoDatabase

    @BeforeTest
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insert(1, false, "今日は", "こんにちは")
                japaneseQueries.insert(1, "今日は")
                readingQueries.insert(1, "こんにちは")
            }
        }
    }

    @Test
    fun testSelectOkuriganaForEntry() = with(database) {
        transaction {
            rubyQueries.insert(ENTRY_ID, "今", "こん")
            rubyQueries.insert(ENTRY_ID, "日", "にち")
            rubyQueries.insert(ENTRY_ID, "は", null)

            val rubies = rubyQueries.selectRubies(ENTRY_ID).executeAsList()
            assertTrue {
                rubies.toTypedArray().contentEquals(
                        arrayOf(
                                SelectRubies("今", "こん"),
                                SelectRubies("日", "にち"),
                                SelectRubies("は", null)
                        )
                )
            }
        }
    }

    @Test
    fun test() = with(database) {
        transaction {
            rubyQueries.insert(ENTRY_ID, "日", "にち")
            val rubies = rubyQueries.selectRubies(ENTRY_ID).executeAsList()
            assertTrue {
                rubies.toTypedArray().contentEquals(
                        arrayOf(SelectRubies("日", "にち"))
                )
            }
        }
    }
}