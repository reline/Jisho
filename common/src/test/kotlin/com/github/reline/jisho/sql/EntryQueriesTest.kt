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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
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
        database = JishoDatabase(driver)
    }

    @Test
    fun testQueryByKanji() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true)
            japaneseQueries.insert(ENTRY_ID, "今日は")
            readingQueries.insert(ENTRY_ID, "こんにちは")

            val entry = entryQueries.selectEntry("今日は").executeAsList().first()
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByLikeKanji() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true)
            japaneseQueries.insert(ENTRY_ID, "今日は")
            readingQueries.insert(ENTRY_ID, "こんにちは")

            val entry = entryQueries.selectEntry("今日").executeAsList().first()
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByReading() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true)
            japaneseQueries.insert(ENTRY_ID, "今日は")
            readingQueries.insert(ENTRY_ID, "こんにちは")

            val entry = entryQueries.selectEntry("こんにちは").executeAsList().first()
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByLikeReading() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true)
            japaneseQueries.insert(ENTRY_ID, "今日は")
            readingQueries.insert(ENTRY_ID, "こんにちは")

            val entry = entryQueries.selectEntry("こんにち").executeAsList().first()
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByGloss() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true)
            japaneseQueries.insert(ENTRY_ID, "今日は")
            readingQueries.insert(ENTRY_ID, "こんにちは")
            senseQueries.insert(ENTRY_ID)
            val senseId = utilQueries.lastInsertRowId().executeAsOne()
            glossQueries.insert(senseId, "hello")

            val entry = entryQueries.selectEntry("hello").executeAsList().first()
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryBySimilarGlossEnd() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true)
            japaneseQueries.insert(ENTRY_ID, "今日は")
            readingQueries.insert(ENTRY_ID, "こんにちは")
            senseQueries.insert(ENTRY_ID)
            val senseId = utilQueries.lastInsertRowId().executeAsOne()
            glossQueries.insert(senseId, "good afternoon")

            val entry = entryQueries.selectEntry("good afterno").executeAsList().first()
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryBySimilarGlossStart() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true)
            japaneseQueries.insert(ENTRY_ID, "今日は")
            readingQueries.insert(ENTRY_ID, "こんにちは")
            senseQueries.insert(ENTRY_ID)
            val senseId = utilQueries.lastInsertRowId().executeAsOne()
            glossQueries.insert(senseId, "good afternoon")

            val entry = entryQueries.selectEntry("afternoon").executeAsList().first()
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Ignore("empty")
    @Test
    fun testOrdered() {
        // todo
    }
}