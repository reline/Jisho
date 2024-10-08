package io.github.reline.jisho.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.Test
import kotlin.properties.Delegates
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GlossQueriesTest {

    private var senseId by Delegates.notNull<Long>()
    private lateinit var database: JishoDatabase

    @BeforeTest
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insert(1, false)
                japaneseQueries.insert("今日は")
                readingQueries.insert("こんにちは")
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
        assertTrue {
            glosses.toTypedArray().contentEquals(
                arrayOf("hello", "good day (daytime greeting)")
            )
        }
    }
}