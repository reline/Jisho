package com.github.reline.jisho.sql

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class EntryKanjiQueriesTest {

    companion object {
        private const val ENTRY_ID = 1289400L
    }

    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver)
    }

    @Test
    fun testKanjiForEntries() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")
            kanjiRadicalQueries.insertKanji("今", 4)
            entryKanjiQueries.insert(ENTRY_ID, utilQueries.lastInsertRowId().executeAsOne())
            kanjiRadicalQueries.insertKanji("日", 4)
            entryKanjiQueries.insert(ENTRY_ID, utilQueries.lastInsertRowId().executeAsOne())

            val kanji = entryKanjiQueries.selectKanjiForEntryId(ENTRY_ID).executeAsList()
            assertEquals(expected = 2, actual = kanji.size)
            assertEquals(expected = "今", actual = kanji[0].value_)
            assertEquals(expected = "日", actual = kanji[1].value_)
        }
    }
}