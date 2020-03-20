package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test

class RubyQueriesTest {

    companion object {
        private const val ENTRY_ID = 1L
    }

    private lateinit var database: JishoDatabase

    // todo: can we write tests to ensure "IF NOT EXISTS" was written?
    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")
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
            assertArrayEquals(
                    arrayOf(
                        SelectRubies.Impl("今", "こん", 1),
                        SelectRubies.Impl("日", "にち", 2),
                        SelectRubies.Impl("は", null, 3)
                    ),
                    rubies.toTypedArray()
            )
        }
    }

    @Test
    fun test() = with(database) {
        transaction {
            rubyQueries.insert("日", "にち")
            val rubyId = rubyQueries.selectRubyId("日", "にち").executeAsOne()
            entryRubyTagQueries.insert(ENTRY_ID, rubyId, 2)
            val rubies = entryRubyTagQueries.selectRubies(ENTRY_ID).executeAsList()
            assertArrayEquals(
                    arrayOf(SelectRubies.Impl("日", "にち", 2)),
                    rubies.toTypedArray()
            )
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
            assertArrayEquals(
                    arrayOf(SelectRubies.Impl("日", "にち", 2)),
                    rubies.toTypedArray()
            )
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