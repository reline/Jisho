package com.github.reline.jisho

import com.github.reline.jisho.sql.JishoDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.reline.jisho.compression.copy
import com.github.reline.jisho.populators.populate
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import kotlin.test.*

class KanjiPopulatorTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: JishoDatabase
    private lateinit var scope: TestScope
    private val jmdict_e by lazy { prepareResource("JMdict_e.xml".toPath()) }
    private val kanjidic2 by lazy { prepareResource("kanjidic2.xml".toPath()) }
    private val radkfile2 by lazy { prepareResource("radkfile2".toPath()) }
    private val radkfilex by lazy { prepareResource("radkfilex".toPath()) }

    @BeforeTest
    fun setUp() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        database = JishoDatabase(driver).also { JishoDatabase.Schema.create(driver) }
        scope = TestScope()
    }

    private fun prepareResource(path: Path): Path {
        val source = FileSystem.RESOURCES.source(path)
        val destination = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.div(path)
        val sink = FileSystem.SYSTEM.sink(destination)
        copy(source, sink)
        return destination
    }

    @AfterTest
    fun tearDown() {
        driver.close()
        scope.cancel()
    }

    @Test
    fun smokeTestKanji() = scope.runTest {
        database.populate(kanji = listOf(kanjidic2.toFile()))
        database.transaction {
            val kanji = database.kanjiRadicalQueries.selectAllKanji().executeAsList()
            assert(kanji.isNotEmpty())
        }
    }

    @Test
    fun hasStrokes() = scope.runTest {
        database.populate(kanji = listOf(kanjidic2.toFile()))
        database.transaction {
            val kanji = database.kanjiRadicalQueries.selectKanji("今").executeAsOne()
            assertEquals(expected = 4, actual = kanji.strokes)
        }
    }

    @Test
    fun smokeTestRadicals() = scope.runTest {
        database.populate(
            kanji = listOf(kanjidic2.toFile()),
            radk = listOf(radkfile2.toFile(), radkfilex.toFile()),
        )
        database.transaction {
            val radicals = database.kanjiRadicalQueries.selectAllRadicals().executeAsList()
            assertTrue(radicals.isNotEmpty())
        }
    }

    @Test
    fun testKanjiForEntries() = scope.runTest {
        val dictionaries = database.populate(listOf(jmdict_e.toFile()))
        database.populate(
            dictionaries = dictionaries,
            kanji = listOf(kanjidic2.toFile()),
        )
        database.transaction {
            val entryId = database.entryQueries.selectEntriesBySimpleJapanese("こんにちは")
                .executeAsList().first().id
            val kanji = database.entryKanjiQueries.selectKanjiForEntryId(entryId).executeAsList()
                .map { it.value_ }
            assertEquals(expected = 2, actual = kanji.size)
            assertTrue(kanji.contains("今"))
            assertTrue(kanji.contains("日"))
        }
    }
}
