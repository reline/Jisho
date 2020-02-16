package com.github.reline.jisho.sql

import com.github.reline.jisho.JishoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test

class EntryQueriesTest {

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
                senseQueries.insertSense()
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
    fun testEntriesForKeywordUsingGlossValue() = with(database) {
        run {
            val entries = entryQueries.entriesForKeyword("hello", { id, _ -> id }).executeAsList()
            assertArrayEquals(longArrayOf(ENTRY_ID), entries.toLongArray())
        }

        run {
            val entries = entryQueries.entriesForKeyword("good day", { id, _ -> id }).executeAsList()
            assertArrayEquals(longArrayOf(ENTRY_ID), entries.toLongArray())
        }

        run {
            val entries = entryQueries.entriesForKeyword("daytime greeting", { id, _ -> id }).executeAsList()
            assertArrayEquals(longArrayOf(ENTRY_ID), entries.toLongArray())
        }
    }

    @Test
    fun testEntriesForKeywordUsingKanjiElementValue() = with(database) {
        run {
            val entries = entryQueries.entriesForKeyword("今日は", { id, _ -> id }).executeAsList()
            assertArrayEquals(longArrayOf(ENTRY_ID), entries.toLongArray())
        }

        run {
            val entries = entryQueries.entriesForKeyword("今", { id, _ -> id }).executeAsList()
            assertArrayEquals(longArrayOf(ENTRY_ID), entries.toLongArray())
        }
    }

    @Test
    fun testEntriesForKeywordUsingReadingValue() = with(database) {
        run {
            val entries = entryQueries.entriesForKeyword("こんにちは", { id, _ -> id }).executeAsList()
            assertArrayEquals(longArrayOf(ENTRY_ID), entries.toLongArray())
        }

        run {
            val entries = entryQueries.entriesForKeyword("こんにち", { id, _ -> id }).executeAsList()
            assertArrayEquals(longArrayOf(ENTRY_ID), entries.toLongArray())
        }
    }
}