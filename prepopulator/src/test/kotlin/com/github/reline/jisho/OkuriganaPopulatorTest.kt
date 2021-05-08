/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import com.github.reline.jisho.dictmodels.jmdict.Dictionary
import com.github.reline.jisho.sql.JishoDatabase
import org.junit.*
import org.junit.Assert.assertTrue
import java.io.File

class OkuriganaPopulatorTest {
    companion object {
        private val testDbPath = "./build/test/${OkuriganaPopulatorTest::class.java.name}/jisho.sqlite"
        private lateinit var database: JishoDatabase
        private val dictionaries = arrayListOf<Pair<Dictionary, OkuriganaEntries>>()
        private lateinit var okuriganaPopulator: OkuriganaPopulator
        private lateinit var dictionaryPopulator: DictionaryPopulator

        @BeforeClass
        @JvmStatic
        fun setupSuite() {
            val db = File(testDbPath)
            db.forceCreate()
            database = provideDatabase("jdbc:sqlite:${testDbPath}")

            okuriganaPopulator = OkuriganaPopulator(database)
            dictionaryPopulator = DictionaryPopulator(database)
            dictionaries.add(Pair(
                    dictionaryPopulator.extractDictionary(File("./build/dict/JMdict_e.xml")),
                    okuriganaPopulator.extractOkurigana(File("./build/dict/JmdictFurigana.json"))
            ))
            dictionaries.add(Pair(
                    dictionaryPopulator.extractDictionary(File("./build/dict/JMnedict.xml")),
                    okuriganaPopulator.extractOkurigana(File("./build/dict/JmnedictFurigana.json"))
            ))
        }

        @AfterClass
        @JvmStatic
        fun teardownSuite() {
            File(testDbPath).delete()
        }
    }

    @Test
    fun smokeTest() {
        assertTrue(dictionaries.isNotEmpty())
        dictionaries.forEach { (dict, okurigana) ->
            dictionaryPopulator.insertEntries(dict.entries)
            okuriganaPopulator.insertOkurigana(dict, okurigana)
        }
    }
}