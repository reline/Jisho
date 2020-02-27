package com.github.reline.jisho.sql

import com.github.reline.jisho.JishoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates.notNull

class PartOfSpeechQueriesTest {
    companion object {
        private const val ENTRY_ID = 1L
    }

    private lateinit var database: JishoDatabase
    private var senseId by notNull<Long>()

    @Before
    fun setup() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insertEntry(ENTRY_ID, false)
                kanjiElementQueries.insertKanji("大人買い", false)
                val kanjiId = kanjiElementQueries.rowid().executeAsOne()
                entryQueries.insertEntryKanjiTag(ENTRY_ID, kanjiId)
                readingQueries.insertReading("おとながい", false, false)
                val readingId = readingQueries.rowid().executeAsOne()
                entryQueries.insertEntryReadingTag(ENTRY_ID, readingId)
                senseQueries.insertSense()
                senseId = senseQueries.rowid().executeAsOne()
                entryQueries.insertEntrySenseTag(ENTRY_ID, senseId)
            }
        }
    }

    @Test
    fun test() = with(database) {
        transaction {
            partOfSpeechQueries.insertPartOfSpeech("Noun")
            senseQueries.insertSensePosTag(senseId, partOfSpeechQueries.rowid("Noun").executeAsOne())
            partOfSpeechQueries.insertPartOfSpeech("Suru verb")
            senseQueries.insertSensePosTag(senseId, partOfSpeechQueries.rowid("Suru verb").executeAsOne())
        }

        val glosses = partOfSpeechQueries.partsOfSpeechForEntry(ENTRY_ID).executeAsList()
        assertArrayEquals(
                arrayOf("Noun", "Suru verb"),
                glosses.toTypedArray()
        )
    }
}