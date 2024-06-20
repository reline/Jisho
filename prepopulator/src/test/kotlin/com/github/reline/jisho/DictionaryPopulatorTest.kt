/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.github.reline.jisho.persistence.Ruby
import com.github.reline.jisho.populators.decodeDictionary
import com.github.reline.jisho.populators.decodeOkurigana
import com.github.reline.jisho.populators.insertDictionary
import com.github.reline.jisho.populators.insertOkurigana
import com.github.reline.jisho.sql.EntryQueries
import com.github.reline.jisho.sql.JishoDatabase
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// todo: move integration tests to functional test suite
class DictionaryPopulatorTest {

    private val resources = FileSystem.RESOURCES
    private val jmdictEntries = "JMdict_e.xml".toPath()
    private val jmdictFurigana = "JmdictFurigana.json".toPath()

    private lateinit var scope: TestScope
    private lateinit var driver: SqlDriver
    private lateinit var database: JishoDatabase
    private lateinit var entryQueries: EntryQueries
    private lateinit var dao: JapaneseMultilingualDao

    @BeforeTest
    fun setUp() {
        scope = TestScope()
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        database = spyk(JishoDatabase(driver).also { JishoDatabase.Schema.create(driver) })
        entryQueries = spyk(database.entryQueries)
        every { database.entryQueries } returns entryQueries
        dao = JapaneseMultilingualDao(database, scope.coroutineContext)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    // todo: benchmark queries; should take ~250ms

    @Test
    fun smokeTest() = scope.runTest {
        resources.read(jmdictEntries) { database.insertDictionary(decodeDictionary(this)) }
        // todo: name entries
        val entries = database.entryQueries.selectAll().executeAsList()
        assertTrue(entries.isNotEmpty())
    }

    @Test
    fun testRubies() = scope.runTest {
        resources.read(jmdictEntries) { database.insertDictionary(decodeDictionary(this)) }
        resources.read(jmdictFurigana) { database.insertOkurigana(decodeOkurigana(this)) }

        val rubies = dao.search("hello").first().rubies
        val expected = setOf(
            Ruby(japanese = "今", okurigana = "こん", position = 0),
            Ruby(japanese = "日", okurigana = "にち", position = 1),
            Ruby(japanese = "は", okurigana = null, position = 2),
        )
        assertEquals(expected, rubies)
    }

    // todo: move these functional tests
    @Ignore("functional test")
    @Test
    fun testHello() = scope.runTest {
        resources.read(jmdictEntries) { database.insertDictionary(decodeDictionary(this)) }

        val results = database.entryQueries.selectEntries("hello").executeAsList()
        val actual = results.map { it.kanji ?: it.reading }
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
        assertEquals(expected.sorted(), actual.sorted())
    }

    @Ignore("functional test")
    @Test
    fun testHouse() = scope.runTest {
        resources.read(jmdictEntries) { database.insertDictionary(decodeDictionary(this)) }

        val results = database.entryQueries.selectEntries("house").executeAsList()
        val actual = results.map { it.kanji ?: it.reading }
        val expected = listOf("家", "家屋", "宅", "住まい", "人家", "宿", "参議院", "衆議院", "ハウス", "部族", "一家", "借家", "お宅", "貸家", "番地", "別荘", "満員")
        assertEquals(expected.sorted(), actual.sorted())
    }

    @Ignore("functional test")
    @Test
    fun test家() = scope.runTest {
        resources.read(jmdictEntries) { database.insertDictionary(decodeDictionary(this)) }

        val results = database.entryQueries.selectEntries("家").executeAsList()
        val actual = results.map{ it.kanji ?: it.reading }
        val expected = listOf("家", "屋", "家族", "家庭", "屋根", "家具")
        assertEquals(expected.sorted(), actual.sorted())
    }

    @Ignore("functional test")
    @Test
    fun test走った() = scope.runTest {
        resources.read(jmdictEntries) { database.insertDictionary(decodeDictionary(this)) }

        val results = dao.search("走った")
        val actual = results.map { it.japanese }
        val expected = listOf("走る")
        assertEquals(expected.sorted(), actual.sorted())
    }
}