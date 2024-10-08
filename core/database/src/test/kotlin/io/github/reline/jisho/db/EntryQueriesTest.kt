package io.github.reline.jisho.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val ENTRY_ID = 1289400L

class EntryQueriesTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: JishoDatabase

    @BeforeTest
    fun setup() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver)
    }

    @Test
    fun testQueryByKanji() = with(database) {
        insertEntry(
            ENTRY_ID,
            japanese = listOf("今日は"),
            readings = listOf("こんにちは"),
            glosses = listOf("good afternoon", "hello"),
        )

        run {
            val entry = entryQueries.selectEntries("今日は").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }

        run {
            val entry = entryQueries.selectEntriesByComplexJapanese("今日は").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByLikeKanji() = with(database) {
        insertEntry(
            ENTRY_ID,
            japanese = listOf("今日は"),
            readings = listOf("こんにちは"),
            glosses = listOf("good afternoon", "hello"),
        )

        run {
            val entry = entryQueries.selectEntries("今日").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }

        run {
            val entry = entryQueries.selectEntriesByComplexJapanese("今日").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByReading() = with(database) {
        insertEntry(
            ENTRY_ID,
            japanese = listOf("今日は"),
            readings = listOf("こんにちは"),
            glosses = listOf("good afternoon", "hello"),
        )

        run {
            val entry = entryQueries.selectEntries("こんにちは").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }

        run {
            val entry = entryQueries.selectEntriesBySimpleJapanese("こんにちは").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByLikeReading() = with(database) {
        insertEntry(
            ENTRY_ID,
            japanese = listOf("今日は"),
            readings = listOf("こんにちは"),
            glosses = listOf("good afternoon", "hello"),
        )

        run {
            val entry = entryQueries.selectEntries("こんにち").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }

        run {
            val entry = entryQueries.selectEntriesBySimpleJapanese("こんにち").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryByGloss() = with(database) {
        insertEntry(
            ENTRY_ID,
            japanese = listOf("今日は"),
            readings = listOf("こんにちは"),
            glosses = listOf("good afternoon", "hello"),
        )

        run {
            val entry = entryQueries.selectEntries("hello").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }

        run {
            val entry = entryQueries.selectEntriesByGloss("hello").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryBySimilarGlossEnd() = with(database) {
        insertEntry(
            ENTRY_ID,
            japanese = listOf("今日は"),
            readings = listOf("こんにちは"),
            glosses = listOf("good afternoon", "hello"),
        )

        run {
            val entry = entryQueries.selectEntries("good afterno").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }

        run {
            val entry = entryQueries.selectEntriesByGloss("good afterno").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testQueryBySimilarGlossStart() = with(database) {
        insertEntry(
            ENTRY_ID,
            japanese = listOf("今日は"),
            readings = listOf("こんにちは"),
            glosses = listOf("good afternoon", "hello"),
        )

        run {
            val entry = entryQueries.selectEntries("afternoon").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }

        run {
            val entry = entryQueries.selectEntriesByGloss("afternoon").executeAsList().first()
            assertTrue(entry.is_common)
            assertEquals("今日は", entry.kanji)
            assertEquals("こんにちは", entry.reading)
        }
    }

    @Test
    fun testOrderedByIsCommonDescending() = with(database) {
        insertEntry(
            ENTRY_ID - 1,
            isCommon = false,
            japanese = listOf("家"),
            readings = listOf("いえ"),
            glosses = listOf("house"),
        )
        insertEntry(
            ENTRY_ID,
            japanese = listOf("今日は"),
            readings = listOf("こんにちは"),
            glosses = listOf("good afternoon", "hello"),
        )
        val results = entryQueries.selectEntries("h").executeAsList()
        assertContentEquals(results.sortedByDescending { it.is_common }, results)
    }
}

private fun JishoDatabase.insertEntry(
    entryId: Long,
    isCommon: Boolean = true,
    japanese: List<String>,
    readings: List<String>,
    glosses: List<String>,
) = transaction {
    entryQueries.insert(entryId, isCommon)
    japanese.forEach {
        japaneseQueries.insert(it)
        val japaneseId = utilQueries.lastInsertRowId().executeAsOne()
        japaneseQueries.insertRelationship(entry_id = entryId, japanese_id = japaneseId)
    }
    readings.forEach {
        readingQueries.insert(it)
        val readingId = utilQueries.lastInsertRowId().executeAsOne()
        readingQueries.insertRelationship(entry_id = entryId, reading_id = readingId)
    }
    senseQueries.insert(entryId)
    val senseId = utilQueries.lastInsertRowId().executeAsOne()
    glosses.forEach {
        glossQueries.insert(senseId, it)
    }
}
