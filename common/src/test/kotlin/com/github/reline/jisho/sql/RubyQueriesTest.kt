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
                entryQueries.insert(1, false)
                japaneseQueries.insert(1, "今日は")
                readingQueries.insert(1, "こんにちは")
            }
        }
    }

    @Test
    fun testSelectOkuriganaForEntry() = with(database) {
        transaction {
            rubyQueries.insert("今", "こん")
            entryRubyTagQueries.insert(ENTRY_ID, utilQueries.lastInsertRowId().executeAsOne(), 1)
            rubyQueries.insert("日", "にち")
            entryRubyTagQueries.insert(ENTRY_ID, utilQueries.lastInsertRowId().executeAsOne(), 2)
            rubyQueries.insert("は", null)
            entryRubyTagQueries.insert(ENTRY_ID, utilQueries.lastInsertRowId().executeAsOne(), 3)

            val rubies = entryRubyTagQueries.selectRubies(ENTRY_ID).executeAsList()
            assertTrue {
                rubies.toTypedArray().contentEquals(
                        arrayOf(
                                SelectRubies("今", "こん", 1),
                                SelectRubies("日", "にち", 2),
                                SelectRubies("は", null, 3)
                        )
                )
            }
        }
    }

    @Test
    fun test() = with(database) {
        transaction {
            rubyQueries.insert("日", "にち")
            val rubyId = rubyQueries.selectRubyId("日", "にち").executeAsOne()
            entryRubyTagQueries.insert(ENTRY_ID, rubyId, 2)
            val rubies = entryRubyTagQueries.selectRubies(ENTRY_ID).executeAsList()
            assertTrue {
                rubies.toTypedArray().contentEquals(
                        arrayOf(SelectRubies("日", "にち", 2))
                )
            }
        }
    }

    @Test
    fun testDuplicate() = with(database) {
        transaction {
            rubyQueries.insert("日", "にち")
            rubyQueries.insert("日", "にち")
            val rubyId = rubyQueries.selectRubyId("日", "にち").executeAsOne()
            entryRubyTagQueries.insert(ENTRY_ID, rubyId, 2)
            val rubies = entryRubyTagQueries.selectRubies(ENTRY_ID).executeAsList()
            assertTrue {
                rubies.toTypedArray().contentEquals(
                        arrayOf(SelectRubies("日", "にち", 2))
                )
            }
        }
    }

    @Test
    fun testDuplicateNull() = with(database) {
        transaction {
            rubyQueries.insert("ゼロ", null)
            rubyQueries.insert("ゼロ", null)
            rubyQueries.selectRubyId("ゼロ", null).executeAsOne()
        }
    }

    @Test
    fun testDuplicateJapanese() = with(database) {
        transaction {
            rubyQueries.insert("ゼロ", "0")
            rubyQueries.insert("ゼロ", "zero")
            rubyQueries.selectRubyId("ゼロ", "zero").executeAsOne()
        }
    }
}