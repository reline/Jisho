/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import com.github.reline.jisho.dictmodels.jmdict.Entry
import com.github.reline.jisho.dictmodels.okurigana.OkuriganaEntry
import org.junit.*
import org.junit.Assert.assertTrue
import java.io.File

@Ignore("Large test")
class OkuriganaPopulatorTest {
    companion object {
        private val testDbPath = "./build/test/${OkuriganaPopulatorTest::class.java.name}"
        private lateinit var entries: List<Entry>
        private lateinit var okurigana: List<OkuriganaEntry>

        @BeforeClass
        @JvmStatic
        fun setupSuite() {
            entries = DictionaryPopulator.extractDictionary(File("./build/dict/JMdict_e.xml")).entries +
                    DictionaryPopulator.extractDictionary(File("./build/dict/JMnedict.xml")).entries
            okurigana = OkuriganaPopulator.extractOkurigana(File("./build/dict/JmdictFurigana.json")) +
                    OkuriganaPopulator.extractOkurigana(File("./build/dict/JmnedictFurigana.json"))
        }
    }

    @Before
    fun setup() {
        val db = File(testDbPath)
        db.parentFile.mkdirs()
        db.delete()
        db.createNewFile()
        buildDir = testDbPath
    }

    @After
    fun teardown() {
        File(testDbPath).delete()
    }

    @Test
    fun smokeTest() {
        assertTrue(entries.isNotEmpty())
        assertTrue(okurigana.isNotEmpty())
        DictionaryPopulator.insertEntries(entries)
        OkuriganaPopulator.insertOkurigana(okurigana)
    }
}