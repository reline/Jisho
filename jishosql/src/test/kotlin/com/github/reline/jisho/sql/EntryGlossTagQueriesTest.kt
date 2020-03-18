package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EntryGlossTagQueriesTest {

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
                glossQueries.insert("hello")
                entryGlossTagQueries.insert(
                        ENTRY_ID,
                        glossQueries.selectGlossIdWhereValueEquals("hello").executeAsOne()
                )
                glossQueries.insert("good day (daytime greeting)")
                entryGlossTagQueries.insert(
                        ENTRY_ID,
                        glossQueries.selectGlossIdWhereValueEquals("good day (daytime greeting)").executeAsOne()
                )
            }
        }
    }

    @Test
    fun test() = with(database) {
        val glosses = entryGlossTagQueries.selectGlossWhereEntryIdEquals(ENTRY_ID).executeAsList()
        assertArrayEquals(
                arrayOf("hello", "good day (daytime greeting)"),
                glosses.toTypedArray()
        )
    }

    @Test
    fun testTagDoesNotUpdate() = with(database) {
        transaction {
            entryQueries.insert(666, false, null, "fake")

            glossQueries.insert("hello")
            val id = glossQueries.selectGlossIdWhereValueEquals("hello").executeAsOne()
            entryGlossTagQueries.insert(666, id)

            val glosses = entryGlossTagQueries.selectGlossWhereEntryIdEquals(ENTRY_ID).executeAsList()
            assertArrayEquals(
                    arrayOf("hello", "good day (daytime greeting)"),
                    glosses.toTypedArray()
            )
            assertEquals("hello", entryGlossTagQueries.selectGlossWhereEntryIdEquals(666).executeAsOne())
        }
    }
}