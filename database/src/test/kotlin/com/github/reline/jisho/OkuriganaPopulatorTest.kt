/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import com.github.reline.jisho.populators.DictionaryPopulator
import com.github.reline.jisho.populators.OkuriganaPopulator
import com.github.reline.jisho.sql.JishoDatabase
import app.cash.sqldelight.db.SqlDriver
import org.junit.Ignore
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

private val testDbPath = "$buildDir/test/${OkuriganaPopulatorTest::class.java.name}/jisho.sqlite"

@Ignore("Large Test")
class OkuriganaPopulatorTest {

    private lateinit var driver: SqlDriver
    private lateinit var database: JishoDatabase
    private lateinit var okuriganaPopulator: OkuriganaPopulator
    private lateinit var dictionaryPopulator: DictionaryPopulator

    @BeforeTest
    fun setUp() {
        val db = File(testDbPath)
        db.forceCreate()
        driver = provideDriver("jdbc:sqlite:$testDbPath")
        database = provideDatabase(driver)

        okuriganaPopulator = OkuriganaPopulator(database)
        dictionaryPopulator = DictionaryPopulator(database)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
        File(testDbPath).delete()
    }

    @Test
    fun smokeTest() = with(database) {
        val dictionaries = dictionaryPopulator.populate(arrayOf(
            File("$buildDir/dict/JMdict_e.xml"),
            File("$buildDir/dict/JMnedict.xml"),
        ))
        OkuriganaPopulator(database).populate(
            dictionaries,
            arrayOf(
                File("$buildDir/dict/JmdictFurigana.json"),
                File("$buildDir/dict/JmnedictFurigana.json"),
            )
        )
        transaction {
            val rubies = rubyQueries.selectAllRubies().executeAsList()
            assert(rubies.isNotEmpty())
        }
    }
}