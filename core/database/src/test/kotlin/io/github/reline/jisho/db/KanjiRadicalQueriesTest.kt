package io.github.reline.jisho.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class KanjiRadicalQueriesTest {

    private lateinit var database: JishoDatabase

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver)
    }

    @Test
    fun testKanji() = with(database) {
        transaction {
            kanjiRadicalQueries.insertKanji("今", 4)
        }
        val id = kanjiRadicalQueries.selectKanji("今").executeAsOne()
        Assert.assertNotNull(id)
    }

    @Test
    fun testRadical() = with(database) {
        transaction {
            kanjiRadicalQueries.insertRadical("个", 2)
        }
        val id = kanjiRadicalQueries.kanjiForRadical("个")
        Assert.assertNotNull(id)
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
            Assert.assertTrue(radicals.containsAll(listOf("一", "个")))
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

            Assert.assertTrue(
                kanjiRadicalQueries.kanjiForRadical("一")
                    .executeAsList().contains("今")
            )
            Assert.assertTrue(
                kanjiRadicalQueries.kanjiForRadical("个")
                    .executeAsList().contains("今")
            )
        }
    }
}