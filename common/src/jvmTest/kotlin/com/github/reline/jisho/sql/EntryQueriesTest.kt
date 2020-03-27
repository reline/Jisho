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
            entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")

            val entry = entryQueries.selectEntry("今日は").executeAsList().first()
            assertEquals(ENTRY_ID, entry.id)
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByLikeKanji() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")

            val entry = entryQueries.selectEntry("今日").executeAsList().first()
            assertEquals(ENTRY_ID, entry.id)
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByReading() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")

            val entry = entryQueries.selectEntry("こんにちは").executeAsList().first()
            assertEquals(ENTRY_ID, entry.id)
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByLikeReading() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")

            val entry = entryQueries.selectEntry("こんにち").executeAsList().first()
            assertEquals(ENTRY_ID, entry.id)
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByGloss() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")
            senseQueries.insert(ENTRY_ID)
            val senseId = utilQueries.lastInsertRowId().executeAsOne()
            glossQueries.insert("hello")
            senseGlossTagQueries.insert(senseId, utilQueries.lastInsertRowId().executeAsOne())

            val entry = entryQueries.selectEntry("hello").executeAsList().first()
            assertEquals(ENTRY_ID, entry.id)
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryBySimilarGlossEnd() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")
            senseQueries.insert(ENTRY_ID)
            val senseId = utilQueries.lastInsertRowId().executeAsOne()
            glossQueries.insert("good afternoon")
            senseGlossTagQueries.insert(senseId, utilQueries.lastInsertRowId().executeAsOne())

            val entry = entryQueries.selectEntry("good afterno").executeAsList().first()
            assertEquals(ENTRY_ID, entry.id)
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryBySimilarGlossStart() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")
            senseQueries.insert(ENTRY_ID)
            val senseId = utilQueries.lastInsertRowId().executeAsOne()
            glossQueries.insert("good afternoon")
            senseGlossTagQueries.insert(senseId, utilQueries.lastInsertRowId().executeAsOne())

            val entry = entryQueries.selectEntry("afternoon").executeAsList().first()
            assertEquals(ENTRY_ID, entry.id)
            assertTrue(entry.isCommon)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testSelectByValues() = with(database) {
        transaction {
            entryQueries.insert(ENTRY_ID, true, "今日は", "こんにちは")
            val id = entryQueries.selectEntryIdWhereValuesEqual("今日は", "こんにちは").executeAsOne()
            assertEquals(ENTRY_ID, id)
        }
    }

    @Ignore("empty")
    @Test
    fun testOrdered() {
        // todo
    }
}