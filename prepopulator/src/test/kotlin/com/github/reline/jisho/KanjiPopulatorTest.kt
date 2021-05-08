package com.github.reline.jisho

import com.github.reline.jisho.dictmodels.jmdict.Dictionary
import com.github.reline.jisho.populators.DictionaryPopulator
import com.github.reline.jisho.populators.KanjiPopulator
import com.github.reline.jisho.sql.JishoDatabase
import com.squareup.sqldelight.db.SqlDriver
import java.io.File
import kotlin.test.*

class KanjiPopulatorTest {

    private lateinit var dbFile: File
    private lateinit var driver: SqlDriver
    private lateinit var database: JishoDatabase
    private lateinit var dictionaryPopulator: DictionaryPopulator
    private lateinit var kanjiPopulator: KanjiPopulator
    private val emptyDictionary = Dictionary().also { it.entries = mutableListOf() }

    @BeforeTest
    fun setUp() {
        val dbPath = "$buildDir/test/${KanjiPopulatorTest::class.simpleName}/jisho.sqlite"
        dbFile = File(dbPath)
        dbFile.forceCreate()
        driver = provideDriver("jdbc:sqlite:$dbPath")
        database = provideDatabase(driver)
        dictionaryPopulator = DictionaryPopulator(database)
        kanjiPopulator = KanjiPopulator(database)
    }

    @Test
    fun smokeTestKanji() = with(database) {
        kanjiPopulator.populate(listOf(emptyDictionary), arrayOf(File("$buildDir/dict/kanjidic2.xml")), emptyArray(), emptyArray())
        transaction {
            val kanji = kanjiRadicalQueries.selectAllKanji().executeAsList()
            assert(kanji.isNotEmpty())
        }
    }

    @Test
    fun hasStrokes() = with(database) {
        kanjiPopulator.populate(listOf(emptyDictionary), arrayOf(File("$buildDir/dict/kanjidic2.xml")), emptyArray(), emptyArray())
        transaction {
            val kanji = kanjiRadicalQueries.selectKanji("亜").executeAsOne()
            assertEquals(expected = 7, actual = kanji.strokes)
        }
    }

    @Test
    fun smokeTestRadicals() = with(database) {
        kanjiPopulator.populate(listOf(emptyDictionary), arrayOf(File("$buildDir/dict/kanjidic2.xml")), arrayOf(
            File("$buildDir/dict/radkfile"),
            File("$buildDir/dict/radkfile2"),
            File("$buildDir/dict/radkfilex"),
        ), arrayOf())
        transaction {
            val radicals = kanjiRadicalQueries.selectAllRadicals().executeAsList()
            assertTrue(radicals.isNotEmpty())
        }
    }

    @Test
    fun testKanjiForEntries() = with(database) {
        val dictionaries = dictionaryPopulator.populate(arrayOf(File("$buildDir/dict/JMdict_e.xml")))
        kanjiPopulator.populate(
            dictionaries,
            arrayOf(File("$buildDir/dict/kanjidic2.xml")),
            emptyArray(),
            emptyArray(),
        )
        transaction {
            val entryId = entryQueries.selectEntriesByComplexJapanese("海豚").executeAsList()
                .first().id
            val kanji = entryKanjiQueries.selectKanjiForEntryId(entryId).executeAsList()
                .map { it.value }
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