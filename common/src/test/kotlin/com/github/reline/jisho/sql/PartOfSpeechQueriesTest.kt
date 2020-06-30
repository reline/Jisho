package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class PartOfSpeechQueriesTest {
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
            assertArrayEquals(
                    arrayOf("interjection", "fake"),
                    pos.toTypedArray()
            )
        }
    }
}