package com.github.reline.jisho.sql

import com.github.reline.jisho.JishoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class KanjiElementQueriesTest {

    companion object {
        private const val ENTRY_ID = 1289400L
    }

    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insertEntry(ENTRY_ID, false)
                kanjiElementQueries.insertKanji("今日は", false)
                entryQueries.insertEntryKanjiTag(ENTRY_ID, kanjiElementQueries.rowid().executeAsOne())
            }
        }
    }

    @Test
    fun selectIdByValue() = with(database) {
        val kanjiId = kanjiElementQueries.selectKanjiByValue("今日は").executeAsOne()
        assertEquals(1, kanjiId)
    }
}