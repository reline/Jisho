package com.github.reline.jisho.sql

import com.github.reline.jisho.JishoDatabase
import com.github.reline.jishodb.SelectOkuriganaForEntry
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test

class OkuriganaQueriesTest {

    companion object {
        private const val ENTRY_ID = 1L
    }

    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insertEntry(ENTRY_ID, false)
                kanjiElementQueries.insertKanji("大人買い", false)
                val kanjiId = kanjiElementQueries.rowid().executeAsOne()
                entryQueries.insertEntryKanjiTag(ENTRY_ID, kanjiId)
                readingQueries.insertReading("おとながい", false, false)
                val readingId = readingQueries.rowid().executeAsOne()
                entryQueries.insertEntryReadingTag(ENTRY_ID, readingId)
                senseQueries.insertSense()
                val senseId = senseQueries.rowid().executeAsOne()
                entryQueries.insertEntrySenseTag(ENTRY_ID, senseId)
                glossQueries.insertGloss("buying a large amount (esp. of collectible items aimed at kids, such as manga)", "en", null)
                senseQueries.insertSenseGlossTag(senseId, glossQueries.rowid().executeAsOne())
            }
        }
    }

    @Test
    fun testSelectOkuriganaForEntry() = with(database) {
        transaction {
            okuriganaQueries.insertOkurigana("大人", "おとな")
            okuriganaQueries.insertEntryOkuriganaTagForWord("大人買い", okuriganaQueries.rowid().executeAsOne())
            okuriganaQueries.insertOkurigana("買", "が")
            okuriganaQueries.insertEntryOkuriganaTagForWord("大人買い", okuriganaQueries.rowid().executeAsOne())
            okuriganaQueries.insertOkuriganaWithoutReading("い")
            okuriganaQueries.insertEntryOkuriganaTagForWord("大人買い", okuriganaQueries.rowid().executeAsOne())
        }

        val actual = okuriganaQueries.selectOkuriganaForEntry(ENTRY_ID).executeAsList()
        assertArrayEquals(arrayOf(
                SelectOkuriganaForEntry.Impl("大人", "おとな"),
                SelectOkuriganaForEntry.Impl("買", "が"),
                SelectOkuriganaForEntry.Impl("い", null)
        ), actual.toTypedArray())
    }
}