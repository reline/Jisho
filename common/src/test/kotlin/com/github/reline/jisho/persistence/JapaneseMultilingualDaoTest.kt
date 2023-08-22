package com.github.reline.jisho.persistence

import com.github.reline.jisho.sql.JishoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JapaneseMultilingualDaoTest {
    private lateinit var database: JishoDatabase

    @BeforeTest
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver)
    }

    @Test
    fun test() = with(database) {
        transaction {
            kanjiRadicalQueries.insertRadical("人", 2)
            kanjiRadicalQueries.insertRadical("戈", 4)
            kanjiRadicalQueries.insertRadical("竹", 6)
            kanjiRadicalQueries.insertRadical("韭", 9)
            kanjiRadicalQueries.insertRadical("乞", 3)
            kanjiRadicalQueries.insertKanji("籤", 23)
            kanjiRadicalQueries.insertRadical("言", 7)
            kanjiRadicalQueries.insertKanji("讖", 24)

            val lottery = kanjiRadicalQueries.selectKanji("籤").executeAsOne().id
            kanjiRadicalQueries.insertKanjiRadicalTag(
                lottery,
                kanjiRadicalQueries.selectRadical("人").executeAsOne().id
            )
            kanjiRadicalQueries.insertKanjiRadicalTag(
                lottery,
                kanjiRadicalQueries.selectRadical("戈").executeAsOne().id
            )
            kanjiRadicalQueries.insertKanjiRadicalTag(
                lottery,
                kanjiRadicalQueries.selectRadical("竹").executeAsOne().id
            )
            kanjiRadicalQueries.insertKanjiRadicalTag(
                lottery,
                kanjiRadicalQueries.selectRadical("韭").executeAsOne().id
            )
            kanjiRadicalQueries.insertKanjiRadicalTag(
                lottery,
                kanjiRadicalQueries.selectRadical("乞").executeAsOne().id
            )

            val omen = kanjiRadicalQueries.selectKanji("讖").executeAsOne().id
            val id1 = kanjiRadicalQueries.selectRadical("人").executeAsOne().id
            kanjiRadicalQueries.insertKanjiRadicalTag(
                omen,
                id1
            )
            val id2 = kanjiRadicalQueries.selectRadical("戈").executeAsOne().id
            kanjiRadicalQueries.insertKanjiRadicalTag(
                omen,
                id2
            )
            val id3 = kanjiRadicalQueries.selectRadical("言").executeAsOne().id
            kanjiRadicalQueries.insertKanjiRadicalTag(
                omen,
                id3
            )
            kanjiRadicalQueries.insertKanjiRadicalTag(
                omen,
                kanjiRadicalQueries.selectRadical("韭").executeAsOne().id
            )

            runBlocking {
                val dao = JapaneseMultilingualDao(database, coroutineContext)
                val relatedRadicals = dao.getRelatedRadicals(listOf(id1, id2, id3))
                val woot = dao.getRelatedRadicalsWickedFast(listOf(id1, id2, id3))
                assertEquals(4, woot.size)
                val results = relatedRadicals.map { it.value_ }
                assertTrue(results.contains("人"))
                assertTrue(results.contains("戈"))
                assertTrue(results.contains("韭"))
                assertTrue(results.contains("言"))
                assertEquals(4, results.size)
            }
        }
    }
}