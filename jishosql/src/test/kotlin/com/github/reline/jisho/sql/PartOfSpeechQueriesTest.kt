package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test

class PartOfSpeechQueriesTest {
    companion object {
        private const val ENTRY_ID = 1L
    }

    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insert(ENTRY_ID, false, "今日は", "こんにちは")
            }
        }
    }

    @Test
    fun test() = with(database) {
        transaction {
            partOfSpeechQueries.insert("interjection")
            entryPosTagQueries.insert(ENTRY_ID, utilQueries.lastInsertRowId().executeAsOne())
            partOfSpeechQueries.insert("fake")
            entryPosTagQueries.insert(ENTRY_ID, utilQueries.lastInsertRowId().executeAsOne())
            val pos = entryPosTagQueries.selectPosWhereEntryIdEquals(ENTRY_ID).executeAsList()
            assertArrayEquals(
                    arrayOf("interjection", "fake"),
                    pos.toTypedArray()
            )
        }
    }
}