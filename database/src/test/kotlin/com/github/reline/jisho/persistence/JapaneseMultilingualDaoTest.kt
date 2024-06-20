package com.github.reline.jisho.persistence

import com.github.reline.jisho.sql.EntryQueries
import com.github.reline.jisho.sql.JishoDatabase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class JapaneseMultilingualDaoTest {

    private lateinit var scope: TestScope
    private lateinit var database: JishoDatabase
    private lateinit var entryQueries: EntryQueries
    private lateinit var dao: JapaneseMultilingualDao

    @BeforeTest
    fun setup() {
        scope = TestScope()
        database = mockk()
        entryQueries = mockk(relaxed = true)
        every { database.entryQueries } returns entryQueries
        dao = JapaneseMultilingualDao(database, scope.coroutineContext)
    }

    @Test
    fun testByGloss() = scope.runTest {
        dao.search("hello")
        verify { entryQueries.selectEntriesByGloss("hello") }
    }

    @Test
    fun testByComplexJapanese() = scope.runTest {
        dao.search("今日は")
        verify { entryQueries.selectEntriesByComplexJapanese("今日 は") }
    }

    @Test
    fun testBySimpleJapanese() = scope.runTest {
        dao.search("こんにちは")
        verify { entryQueries.selectEntriesBySimpleJapanese("こんにちは") }
    }

    @Test
    fun testByRaw() = scope.runTest {
        dao.search("こんにちは!")
        verify { entryQueries.selectEntries("こんにちは!") }
    }
}