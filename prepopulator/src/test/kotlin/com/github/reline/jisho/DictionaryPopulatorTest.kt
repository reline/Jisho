/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.github.reline.jisho.sql.JishoDatabase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.rules.ErrorCollector
import java.io.File

class DictionaryPopulatorTest {

    companion object {
        private val testDbPath = "./build/test/${DictionaryPopulatorTest::class.java.name}/jisho.sqlite"
        private lateinit var database: JishoDatabase

        @BeforeClass @JvmStatic
        fun setupSuite() {
            val db = File(testDbPath)
            db.forceCreate()
            database = provideDatabase("jdbc:sqlite:$testDbPath")

            val dictionaryPopulator = DictionaryPopulator(database)
            dictionaryPopulator.insertDictionary(dictionaryPopulator.extractDictionary(File("./build/dict/JMdict_e.xml")))
            dictionaryPopulator.insertDictionary(dictionaryPopulator.extractDictionary(File("./build/dict/JMnedict.xml")))
        }

        @AfterClass @JvmStatic
        fun teardownSuite() {
            File(testDbPath).delete()
        }
    }

    @JvmField
    @Rule
    var collector = ErrorCollector()

    // todo: benchmark queries; should take ~250ms

    @Test
    fun testHello() = with(database) {
        val results = entryQueries.selectEntries("hello").executeAsList()
        val actual = results.map{ it.kanji ?: it.reading }
        // missing from dictionary: アンニョンハシムニカ, チョリース, やあやあ, ちわ, いよう, 挨拶まわり
        /**
         * <entry>
        <ent_seq>1924610</ent_seq>
        <r_ele>
        <reb>ハローワーク</reb>
        </r_ele>
        <r_ele>
        <reb>ハロー・ワーク</reb>
        </r_ele>
        <sense>
        <pos>&n-pr;</pos>
        <xref>公共職業安定所</xref>
        <misc>&col;</misc>
        <lsource ls_wasei="y"/>
        <gloss>Hello Work</gloss>
        <gloss>Public Employment Security Office</gloss>
        </sense>
        </entry>

        <entry>
        <ent_seq>1631140</ent_seq>
        <k_ele>
        <keb>公共職業安定所</keb>
        <ke_pri>news2</ke_pri>
        <ke_pri>nf25</ke_pri>
        </k_ele>
        <r_ele>
        <reb>こうきょうしょくぎょうあんていじょ</reb>
        <re_pri>news2</re_pri>
        <re_pri>nf25</re_pri>
        </r_ele>
        <sense>
        <pos>&n-pr;</pos>
        <xref>ハローワーク</xref>
        <gloss>Public Employment Security Office</gloss>
        <gloss>PESO</gloss>
        </sense>
        </entry>
         */
        val expected = listOf("今日は", "もしもし", "こんにちわ", "ハイサイ", "アニョハセヨ", "ニーハオ", "ハロー", "どうも", "はいはい", "ハローワーク", "ハローページ", "ポケハロ", "ほいほい"/*, "公共職業安定所" todo: support xref*/)
        expected.forEach {
            collector.checkSucceeds {
                assertTrue("Missing $it", actual.contains(it))
            }
        }
        actual.forEach {
            if (!expected.contains(it)) {
                println("Unexpected result: $it")
            }
        }
    }

    @Test
    fun testHouse() = with(database) {
        val results = entryQueries.selectEntries("house").executeAsList()
        val actual = results.map{ it.kanji ?: it.reading }
        val expected = listOf("家", "家屋", "宅", "住まい", "人家", "宿", "参議院", "衆議院", "ハウス", "部族", "一家", "借家", "お宅", "貸家", "番地", "別荘", "満員")
        expected.forEach {
            collector.checkSucceeds {
                assertTrue("Missing $it", actual.contains(it))
            }
        }
    }

    @Test
    fun test家() = with(database) {
        val results = entryQueries.selectEntries("家").executeAsList()
        val actual = results.map{ it.kanji ?: it.reading }
        val expected = listOf("家", "屋", "家族", "家庭", "屋根", "家具")
        expected.forEach {
            collector.checkSucceeds {
                assertTrue("Missing $it", actual.contains(it))
            }
        }
    }

    @Test
    fun test走った() = runBlocking {
        val dao = JapaneseMultilingualDao(database, coroutineContext)
        val results = dao.search("走った")
        val actual = results.map { it.japanese }
        val expected = listOf("走る")
        expected.forEach {
            collector.checkSucceeds {
                assertTrue("Missing $it", actual.contains(it))
            }
        }
    }
}