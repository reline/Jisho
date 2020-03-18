package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EntryPosTagQueriesTest {
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
                entryQueries.insert(ENTRY_ID, false, "今日は", "こんにちは")
                partOfSpeechQueries.insert("interjection")
                entryPosTagQueries.insert(
                        ENTRY_ID,
                        partOfSpeechQueries.selectPosIdWhereValueEquals("interjection").executeAsOne()
                )
            }
        }
    }

    @Test
    fun test() = with(database) {
        val glosses = entryPosTagQueries.selectPosWhereEntryIdEquals(ENTRY_ID).executeAsList()
        assertArrayEquals(
                arrayOf("interjection"),
                glosses.toTypedArray()
        )
    }

    @Test
    fun testTagDoesNotUpdate() = with(database) {
        transaction {
            entryQueries.insert(666, false, null, "fake")

            partOfSpeechQueries.insert("interjection")
            val id = partOfSpeechQueries.selectPosIdWhereValueEquals("interjection").executeAsOne()
            entryPosTagQueries.insert(666, id)

            val glosses = entryPosTagQueries.selectPosWhereEntryIdEquals(ENTRY_ID).executeAsList()
            assertArrayEquals(
                    arrayOf("interjection"),
                    glosses.toTypedArray()
            )
            assertEquals("interjection", entryPosTagQueries.selectPosWhereEntryIdEquals(666).executeAsOne())
        }
    }
}