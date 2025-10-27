package com.github.reline.jisho.jmdict

import com.github.reline.jisho.dictmodels.jmdict.decodeDictionary
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.test.Test
import kotlin.test.assertEquals

class JmdictSerializationTest {
    private val resources = FileSystem.RESOURCES
    private val jmdictEntries = "JMdict_e.xml".toPath()

    @Test
    fun testDeserializer() {
        val dictionary = resources.read(jmdictEntries) {
            decodeDictionary(this)
        }
        with(dictionary) {
            assertEquals(1, entries.size)
            with (entries.first()) {
                assertEquals(1289400, id)
                assertEquals("今日は", kanji?.first()?.value)
                assertEquals(2, readings.size)
                with (senses.first()) {
                    assertEquals("interjection (kandoushi)", partsOfSpeech?.first())
                    assertEquals("word usually written using kana alone", miscellaneous?.first())
                    assertEquals("hello", glosses?.get(0))
                    assertEquals("good day (daytime greeting)", glosses?.get(1))
                }
            }
        }
    }
}