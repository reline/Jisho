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
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver.Companion.IN_MEMORY
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KanjiRadicalQueriesTest {

    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver)
    }

    @Test
    fun testKanji() = with(database) {
        transaction {
            kanjiRadicalQueries.insertKanji("今", 4)
        }
        val id = kanjiRadicalQueries.selectKanji("今").executeAsOne()
        assertNotNull(id)
    }

    @Test
    fun testRadical() = with(database) {
        transaction {
            kanjiRadicalQueries.insertRadical("个", 2)
        }
        val id = kanjiRadicalQueries.kanjiForRadical("个")
        assertNotNull(id)
    }

    @Test
    fun testRadicalsForKanji() = with(database) {
        transaction {
            kanjiRadicalQueries.insertRadical("一", 1)
            kanjiRadicalQueries.insertRadical("个", 2)
            kanjiRadicalQueries.insertKanji("今", 4)

            val kanjiId = kanjiRadicalQueries.selectKanji("今").executeAsOne().id
            kanjiRadicalQueries.insertKanjiRadicalTag(
                    kanjiId,
                    kanjiRadicalQueries.selectRadical("一").executeAsOne().id
            )
            kanjiRadicalQueries.insertKanjiRadicalTag(
                    kanjiId,
                    kanjiRadicalQueries.selectRadical("个").executeAsOne().id
            )

            val radicals = kanjiRadicalQueries.radicalsForKanji("今").executeAsList()
            assertTrue(radicals.containsAll(listOf("一", "个")))
        }
    }

    @Test
    fun testKanjiForRadicals() = with(database) {
        transaction {
            kanjiRadicalQueries.insertRadical("一", 1)
            kanjiRadicalQueries.insertRadical("个", 2)
            kanjiRadicalQueries.insertKanji("今", 4)

            val kanjiId = kanjiRadicalQueries.selectKanji("今").executeAsOne().id
            kanjiRadicalQueries.insertKanjiRadicalTag(
                    kanjiId,
                    kanjiRadicalQueries.selectRadical("一").executeAsOne().id
            )
            kanjiRadicalQueries.insertKanjiRadicalTag(
                    kanjiId,
                    kanjiRadicalQueries.selectRadical("个").executeAsOne().id
            )

            assertTrue(kanjiRadicalQueries.kanjiForRadical("一")
                    .executeAsList().contains("今"))
            assertTrue(kanjiRadicalQueries.kanjiForRadical("个")
                    .executeAsList().contains("今"))
        }
    }
}