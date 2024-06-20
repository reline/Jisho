/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.sql

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver.Companion.IN_MEMORY
import org.junit.Test
import kotlin.properties.Delegates.notNull
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class RubyQueriesTest {

    private lateinit var database: JishoDatabase

    private val entryId = 1L
    private var japaneseId by notNull<Long>()
    private var readingId by notNull<Long>()

    @BeforeTest
    fun setup() {
        val driver = JdbcSqliteDriver(IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        database = JishoDatabase(driver).apply {
            transaction {
                entryQueries.insert(entryId, false)
                japaneseQueries.insert("今日は")
                japaneseId = utilQueries.lastInsertRowId().executeAsOne()
                readingQueries.insert("こんにちは")
                readingId = utilQueries.lastInsertRowId().executeAsOne()
            }
        }
    }

    @Test
    fun testSelectOkuriganaForEntry() = with(database) {
        val rubies = listOf(
            SelectRubies(japanese = "今", okurigana = "こん", position = 0),
            SelectRubies(japanese = "日", okurigana = "にち", position = 1),
            SelectRubies(japanese = "は", okurigana = null, position = 2),
        )

        transaction {
            rubies.forEach { (japanese, okurigana, position) ->
                rubyQueries.insert(japanese = japanese, okurigana = okurigana)
                rubyQueries.insertRelationship(
                    japanese_id = japaneseId,
                    reading_id = readingId,
                    utilQueries.lastInsertRowId().executeAsOne(),
                    position,
                )
            }
        }

        val actual = rubyQueries.selectRubies(
            japanese_id = japaneseId,
            reading_id = readingId,
        ).executeAsList()
        assertEquals(rubies, actual)
    }

    @Test
    fun testRubiesAreUnique() = with(database) {
        transaction {
            val japanese = "日"
            val okurigana = "にち"
            rubyQueries.insert(japanese = japanese, okurigana = okurigana)
            val expected = rubyQueries.selectRuby(japanese = japanese, okurigana = okurigana).executeAsOne()
            rubyQueries.insert(japanese = japanese, okurigana = okurigana)
            val actual = rubyQueries.selectRuby(japanese = japanese, okurigana = okurigana).executeAsOne()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun testNullRubiesAreUnique() = with(database) {
        transaction {
            val japanese = "日"
            val okurigana = null
            rubyQueries.insert(japanese = japanese, okurigana = okurigana)
            val expected = utilQueries.lastInsertRowId().executeAsOne()
            rubyQueries.insert(japanese = japanese, okurigana = okurigana)
            val actual = utilQueries.lastInsertRowId().executeAsOne()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun testRubyRelationshipsAreUnique() = with(database) {
        transaction {
            val japanese = "日"
            val okurigana = "にち"
            rubyQueries.insert(japanese = japanese, okurigana = okurigana)
            val rubyId = utilQueries.lastInsertRowId().executeAsOne()
            rubyQueries.insertRelationship(
                japanese_id = japaneseId,
                reading_id = readingId,
                ruby_id = rubyId,
                1,
            )
            val expected = utilQueries.lastInsertRowId().executeAsOne()
            rubyQueries.insertRelationship(
                japanese_id = japaneseId,
                reading_id = readingId,
                ruby_id = rubyId,
                1,
            )
            assertEquals(expected, utilQueries.lastInsertRowId().executeAsOne())
        }
    }

    // todo: unit test for case when primary key is set to AUTOINCREMENT as it does not work
}