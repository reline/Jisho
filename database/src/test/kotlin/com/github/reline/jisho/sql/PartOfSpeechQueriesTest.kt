/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.sql

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