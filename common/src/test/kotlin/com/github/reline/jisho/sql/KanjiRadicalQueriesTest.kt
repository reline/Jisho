/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

//package com.github.reline.jisho.sql
//
//import com.squareup.sqldelight.db.SqlDriver
//import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
//import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
//import org.junit.Assert.assertNotNull
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Test
//
//class KanjiRadicalQueriesTest {
//
//    private lateinit var database: JishoDatabase
//
//    @Before
//    fun setup() {
//        val driver: SqlDriver = JdbcSqliteDriver(IN_MEMORY)
//        JishoDatabase.Schema.create(driver)
//        database = JishoDatabase(driver)
//    }
//
//    @Test
//    fun testKanji() = with(database) {
//        transaction {
//            kanjiRadicalQueries.insertKanji("今")
//        }
//        val id = kanjiRadicalQueries.kanjiId("今").executeAsOne()
//        assertNotNull(id)
//    }
//
//    @Test
//    fun testRadical() = with(database) {
//        transaction {
//            kanjiRadicalQueries.insertRadical("个", 2)
//        }
//        val id = kanjiRadicalQueries.radicalId("个")
//        assertNotNull(id)
//    }
//
//    @Test
//    fun testRadicalsForKanji() = with(database) {
//        transaction {
//            kanjiRadicalQueries.insertRadical("一", 1)
//            kanjiRadicalQueries.insertRadical("个", 2)
//            kanjiRadicalQueries.insertKanji("今")
//
//            val kanjiId = kanjiRadicalQueries.kanjiId("今").executeAsOne()
//            kanjiRadicalQueries.insertKanjiRadicalTag(
//                    kanjiId,
//                    kanjiRadicalQueries.radicalId("一").executeAsOne()
//            )
//            kanjiRadicalQueries.insertKanjiRadicalTag(
//                    kanjiId,
//                    kanjiRadicalQueries.radicalId("个").executeAsOne()
//            )
//
//            val radicals = kanjiRadicalQueries.radicalsForKanji("今").executeAsList()
//            assertTrue(radicals.containsAll(listOf("一", "个")))
//        }
//    }
//
//    @Test
//    fun testKanjiForRadicals() = with(database) {
//        transaction {
//            kanjiRadicalQueries.insertRadical("一", 1)
//            kanjiRadicalQueries.insertRadical("个", 2)
//            kanjiRadicalQueries.insertKanji("今")
//
//            val kanjiId = kanjiRadicalQueries.kanjiId("今").executeAsOne()
//            kanjiRadicalQueries.insertKanjiRadicalTag(
//                    kanjiId,
//                    kanjiRadicalQueries.radicalId("一").executeAsOne()
//            )
//            kanjiRadicalQueries.insertKanjiRadicalTag(
//                    kanjiId,
//                    kanjiRadicalQueries.radicalId("个").executeAsOne()
//            )
//
//            assertTrue(kanjiRadicalQueries.kanjiForRadical("一")
//                    .executeAsList().contains("今"))
//            assertTrue(kanjiRadicalQueries.kanjiForRadical("个")
//                    .executeAsList().contains("今"))
//        }
//    }
//}