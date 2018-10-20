package com.github.reline.jisho

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.github.reline.jisho.persistence.JapaneseMultilingualDatabase
import com.github.reline.jisho.persistence.entities.Entry
import com.github.reline.jisho.persistence.entities.Kanji
import com.github.reline.jisho.persistence.entities.KanjiPriority
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var dao: JapaneseMultilingualDao
    private lateinit var database: JapaneseMultilingualDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        // TODO: use an in memory database as well. this is just for analyzing if the tables are being built correctly and adding tests to the db module.
        database = Room.databaseBuilder(context, JapaneseMultilingualDatabase::class.java, "jisho-test.sqlite").build()
        dao = database.getDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun test() {
        val kanji = listOf(Kanji("今日は", priorities = listOf(KanjiPriority("ichi1"))))
        dao.insertEntry(Entry(1, kanji))
        val entries = dao.getAllEntries()
        assertEquals(1, entries.size)
    }

}