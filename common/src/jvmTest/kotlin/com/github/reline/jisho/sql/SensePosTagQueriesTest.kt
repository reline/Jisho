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
import org.junit.Test
import kotlin.properties.Delegates
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SensePosTagQueriesTest {
    private var senseId by Delegates.notNull<Long>()
    private lateinit var database: JishoDatabase

    @BeforeTest
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insert(1, false, "今日は", "こんにちは")
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
        assertTrue {
            glosses.toTypedArray().contentEquals(
                    arrayOf("interjection")
            )
        }
    }

    @Test
    fun testTagDoesNotUpdate() = with(database) {
        transaction {
            entryQueries.insert(666, false, "", "")
            senseQueries.insert(666)
            val senseId = utilQueries.lastInsertRowId().executeAsOne()

            partOfSpeechQueries.insert("interjection")
            val id = partOfSpeechQueries.selectPosIdWhereValueEquals("interjection").executeAsOne()
            sensePosTagQueries.insert(senseId, id)

            val glosses = sensePosTagQueries.selectPosWhereSenseIdEquals(senseId).executeAsList()
            assertTrue {
                glosses.toTypedArray().contentEquals(
                        arrayOf("interjection")
                )
            }
            assertEquals("interjection", sensePosTagQueries.selectPosWhereSenseIdEquals(senseId).executeAsOne())
        }
    }
}