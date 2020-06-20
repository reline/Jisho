package com.github.reline.jisho

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.io.File

class DictionaryPopulatorTest {

    private val jmdict = File("./build/dict/JMdict_e.xml")
    private val testDbPath = "./build/test/jmdict.sqlite"

    @Before
    fun setup() {
        val db = File(testDbPath)
        db.parentFile.mkdirs()
        db.delete()
        db.createNewFile()
        databasePath = testDbPath
    }

    @After
    fun teardown() {
        File(testDbPath).delete()
    }

    @Ignore("Large test")
    @Test
    fun testGlosses() = runBlocking {
        val dict = DictionaryPopulator.extractDictionary(jmdict)
        DictionaryPopulator.insertGlosses(dict.entries)
        assertFalse(driver.hasDuplicateValues("Gloss", "value"))
    }
}