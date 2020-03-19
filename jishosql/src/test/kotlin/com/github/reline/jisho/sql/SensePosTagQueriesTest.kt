package com.github.reline.jisho.sql

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

class SensePosTagQueriesTest {
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
                partOfSpeechQueries.insert("interjection")
                sensePosTagQueries.insert(
                        senseId,
                        partOfSpeechQueries.selectPosIdWhereValueEquals("interjection").executeAsOne()
                )
            }
        }
    }

    @Test
    fun test() = with(database) {
        val glosses = sensePosTagQueries.selectPosWhereSenseIdEquals(senseId).executeAsList()
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
            sensePosTagQueries.insert(666, id)

            val glosses = sensePosTagQueries.selectPosWhereSenseIdEquals(senseId).executeAsList()
            assertArrayEquals(
                    arrayOf("interjection"),
                    glosses.toTypedArray()
            )
            assertEquals("interjection", sensePosTagQueries.selectPosWhereSenseIdEquals(666).executeAsOne())
        }
    }
}