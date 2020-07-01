package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class GlossQueriesTest {

    private var senseId by Delegates.notNull<Long>()
    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insert(1, false)
                japaneseQueries.insert(1, "今日は")
                readingQueries.insert(1, "こんにちは")
                senseQueries.insert(1)
                senseId = utilQueries.lastInsertRowId().executeAsOne()
                glossQueries.insert(senseId, "hello")
                glossQueries.insert(senseId, "good day (daytime greeting)")
            }
        }
    }

    @Test
    fun testInsertAllowDuplicateGloss() = with(database) {
        transaction {
            val expected = glossQueries.selectGlossIdWhereValueEquals("hello").executeAsList()
            assertEquals(1, expected.size)
            glossQueries.insert(senseId, "hello")
            val actual = glossQueries.selectGlossIdWhereValueEquals("hello").executeAsList()
            assertEquals(2, actual.size)
        }
    }

    @Test
    fun test() = with(database) {
        val glosses = glossQueries.selectGlossWhereSenseIdEquals(senseId).executeAsList()
        Assert.assertArrayEquals(
                arrayOf("hello", "good day (daytime greeting)"),
                glosses.toTypedArray()
        )
    }
}