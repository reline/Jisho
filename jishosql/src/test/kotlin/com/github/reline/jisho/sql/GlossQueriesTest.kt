package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GlossQueriesTest {

    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver)
    }

    @Test
    fun testInsertIgnoreDuplicate() = with(database) {
        transaction {
            glossQueries.insert("hello")
            val expected = glossQueries.selectGlossIdWhereValueEquals("hello").executeAsOne()
            glossQueries.insert("hello")
            val actual = glossQueries.selectGlossIdWhereValueEquals("hello").executeAsOne()
            assertEquals(expected, actual)
        }
    }
}