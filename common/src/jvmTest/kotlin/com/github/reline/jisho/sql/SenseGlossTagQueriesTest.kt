package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class SenseGlossTagQueriesTest {

    private var senseId by Delegates.notNull<Long>()
    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insert(1, false, "今日は", "こんにちは")
                senseQueries.insert(1)
                senseId = utilQueries.lastInsertRowId().executeAsOne()
                glossQueries.insert("hello")
                senseGlossTagQueries.insert(
                        senseId,
                        glossQueries.selectGlossIdWhereValueEquals("hello").executeAsOne()
                )
                glossQueries.insert("good day (daytime greeting)")
                senseGlossTagQueries.insert(
                        senseId,
                        glossQueries.selectGlossIdWhereValueEquals("good day (daytime greeting)").executeAsOne()
                )
            }
        }
    }

    @Test
    fun test() = with(database) {
        val glosses = senseGlossTagQueries.selectGlossWhereSenseIdEquals(senseId).executeAsList()
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
            senseGlossTagQueries.insert(666, id)

            val glosses = senseGlossTagQueries.selectGlossWhereSenseIdEquals(senseId).executeAsList()
            assertArrayEquals(
                    arrayOf("hello", "good day (daytime greeting)"),
                    glosses.toTypedArray()
            )
            assertEquals("hello", senseGlossTagQueries.selectGlossWhereSenseIdEquals(666).executeAsOne())
        }
    }
}