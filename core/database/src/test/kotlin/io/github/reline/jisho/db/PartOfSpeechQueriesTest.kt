package io.github.reline.jisho.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.Test
import kotlin.properties.Delegates
import kotlin.test.BeforeTest
import kotlin.test.assertTrue

class PartOfSpeechQueriesTest {
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
            }
        }
    }

    @Test
    fun test() = with(database) {
        transaction {
            partOfSpeechQueries.insert("interjection")
            sensePosTagQueries.insert(senseId, utilQueries.lastInsertRowId().executeAsOne())
            partOfSpeechQueries.insert("fake")
            sensePosTagQueries.insert(senseId, utilQueries.lastInsertRowId().executeAsOne())
            val pos = sensePosTagQueries.selectPosWhereSenseIdEquals(senseId).executeAsList()
            assertTrue {
                pos.toTypedArray().contentEquals(
                    arrayOf("interjection", "fake")
                )
            }
        }
    }
}