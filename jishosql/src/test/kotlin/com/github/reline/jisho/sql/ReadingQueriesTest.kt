package com.github.reline.jisho.sql

import com.github.reline.jisho.JishoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ReadingQueriesTest {
    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver)
    }

    @Test
    fun testInsertDuplicateReading() = with(database) {
        transaction {
            readingQueries.insertReading("こんにちは", false, false)
            val expected = readingQueries.selectReadingByValue("こんにちは").executeAsOne()
            readingQueries.insertReading("こんにちは", false, false)
            val actual = readingQueries.selectReadingByValue("こんにちは").executeAsOne()
            assertEquals(expected, actual)
        }
    }
}