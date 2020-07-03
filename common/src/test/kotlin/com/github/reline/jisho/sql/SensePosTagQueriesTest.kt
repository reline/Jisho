/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

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
                entryQueries.insert(1, false)
                japaneseQueries.insert(1, "今日は")
                readingQueries.insert(1, "こんにちは")
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
            entryQueries.insert(666, false)
            senseQueries.insert(666)
            val senseId = utilQueries.lastInsertRowId().executeAsOne()

            partOfSpeechQueries.insert("interjection")
            val id = partOfSpeechQueries.selectPosIdWhereValueEquals("interjection").executeAsOne()
            sensePosTagQueries.insert(senseId, id)

            val glosses = sensePosTagQueries.selectPosWhereSenseIdEquals(senseId).executeAsList()
            assertArrayEquals(
                    arrayOf("interjection"),
                    glosses.toTypedArray()
            )
            assertEquals("interjection", sensePosTagQueries.selectPosWhereSenseIdEquals(senseId).executeAsOne())
        }
    }
}