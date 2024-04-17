package com.github.reline.jisho

import com.github.reline.jisho.dictmodels.jmdict.Dictionary
import com.github.reline.jisho.sql.JishoDatabase
import app.cash.sqldelight.db.SqlDriver
import com.github.reline.jisho.populators.populate
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import java.io.File
import kotlin.test.*

class KanjiPopulatorTest {

    private lateinit var dbFile: File
    private lateinit var driver: SqlDriver
    private lateinit var database: JishoDatabase
    private lateinit var scope: TestScope
    private val emptyDictionary = Dictionary().also { it.entries = mutableListOf() }

    @BeforeTest
    fun setUp() {
        val dbPath = "$buildDir/test/${KanjiPopulatorTest::class.simpleName}/jisho.sqlite"
        dbFile = File(dbPath)
        dbFile.forceCreate()
        driver = dbFile.jdbcSqliteDriver
        database = JishoDatabase(driver).also { JishoDatabase.Schema.create(driver) }
        scope = TestScope()
    }

    @Test
    fun smokeTestKanji() = scope.runTest {
        database.populate(listOf(emptyDictionary), listOf(File("$buildDir/dict/kanjidic2.xml")), emptyList(), emptyList())
        database.transaction {
            val kanji = database.kanjiRadicalQueries.selectAllKanji().executeAsList()
            assert(kanji.isNotEmpty())
        }
    }

    @Test
    fun hasStrokes() = scope.runTest {
        database.populate(listOf(emptyDictionary), listOf(File("$buildDir/dict/kanjidic2.xml")), emptyList(), emptyList())
        database.transaction {
            val kanji = database.kanjiRadicalQueries.selectKanji("亜").executeAsOne()
            assertEquals(expected = 7, actual = kanji.strokes)
        }
    }

    @Test
    fun smokeTestRadicals() = scope.runTest {
        database.populate(listOf(emptyDictionary), listOf(File("$buildDir/dict/kanjidic2.xml")), listOf(
            File("$buildDir/dict/radkfile"),
            File("$buildDir/dict/radkfile2"),
            File("$buildDir/dict/radkfilex"),
        ), emptyList())
        database.transaction {
            val radicals = database.kanjiRadicalQueries.selectAllRadicals().executeAsList()
            assertTrue(radicals.isNotEmpty())
        }
    }

    @Test
    fun testKanjiForEntries() = scope.runTest {
        val dictionaries = database.populate(listOf(File("$buildDir/dict/JMdict_e.xml")))
        database.populate(
            dictionaries,
            listOf(File("$buildDir/dict/kanjidic2.xml")),
            emptyList(),
            emptyList(),
        )
        database.transaction {
            val entryId = database.entryQueries.selectEntriesByComplexJapanese("海豚").executeAsList()
                .first().id
            val kanji = database.entryKanjiQueries.selectKanjiForEntryId(entryId).executeAsList()
                .map { it.value_ }
            assertEquals(expected = 2, actual = kanji.size)
            assertTrue(kanji.contains("海"))
            assertTrue(kanji.contains("豚"))
        }
    }

    @AfterTest
    fun tearDown() {
        driver.close()
        dbFile.delete()
    }

}