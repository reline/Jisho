package com.github.reline.jishodb

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.io.File

class DictionaryRunnerTest {

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
        val dict = DictionaryRunner.extractDictionary(jmdict)
        DictionaryRunner.insertGlosses(dict.entries)
        assertFalse(driver.hasDuplicateValues("Gloss", "value"))
    }
}