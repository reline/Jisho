package com.github.reline.jisho.sql

import com.github.reline.jisho.JishoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test

class GlossQueriesTest {

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
                val kanjiId = kanjiElementQueries.rowid().executeAsOne()
                entryQueries.insertEntryKanjiTag(ENTRY_ID, kanjiId)
                readingQueries.insertReading("こんにちは", false, false)
                val readingId = readingQueries.rowid().executeAsOne()
                entryQueries.insertEntryReadingTag(ENTRY_ID, readingId)
                senseQueries.insertSense(1)
                val senseId = senseQueries.rowid().executeAsOne()
                entryQueries.insertEntrySenseTag(ENTRY_ID, senseId)
                glossQueries.insertGloss("hello", "en", null)
                senseQueries.insertSenseGlossTag(senseId, glossQueries.rowid().executeAsOne())
                glossQueries.insertGloss("good day (daytime greeting)", "en", null)
                senseQueries.insertSenseGlossTag(senseId, glossQueries.rowid().executeAsOne())
            }
        }
    }

    @Test
    fun test() = with(database) {
        val glosses = glossQueries.glossForEntry(ENTRY_ID).executeAsList()
        assertArrayEquals(
                arrayOf("hello", "good day (daytime greeting)"),
                glosses.toTypedArray()
        )
    }
}