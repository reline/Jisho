package com.github.reline.jisho.persistence

import com.github.reline.jisho.sql.JishoDatabase
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class JapaneseMultilingualDaoTest {

    private lateinit var database: JishoDatabase
    private lateinit var dao: JapaneseMultilingualDao

    @BeforeTest
    fun setup() {
        database = TODOJishoDatabase()
    }

    @Test
    fun `test 日`() = runBlocking {
        dao = JapaneseMultilingualDao(database, coroutineContext)
        val actual = dao.getRadicalsFiltered(listOf('日'))
        val expected = listOf('寸', '土', '門')
        assertEquals(expected, actual)
    }

    @Test
    fun `test 門`() = runBlocking {
        dao = JapaneseMultilingualDao(database, coroutineContext)
        val actual = dao.getRadicalsFiltered(listOf('門'))
        val expected = listOf('日')
        assertEquals(expected, actual)
    }

    @Test
    fun `test 寸`() = runBlocking {
        dao = JapaneseMultilingualDao(database, coroutineContext)
        val actual = dao.getRadicalsFiltered(listOf('寸'))
        val expected = listOf('土', '日')
        assertEquals(expected, actual)
    }

    @Test
    fun `test 土`() = runBlocking {
        dao = JapaneseMultilingualDao(database, coroutineContext)
        val actual = dao.getRadicalsFiltered(listOf('土'))
        val expected = listOf('寸', '日')
        assertEquals(expected, actual)
    }
}