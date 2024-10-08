/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package io.github.reline.jisho.unicode

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JapaneseCodePointsTest {
    @Test
    fun `first hiragana should be a kana`() {
        assertTrue(0x3040.isKana())
    }

    @Test
    fun `last katakana should be a kana`() {
        assertTrue(0x30FF.isKana())
    }

    @Test
    fun `random kana is kana`() {
        assertTrue(0x3092.isKana())
    }

    @Test
    fun `ditto mark is a kana`() {
        assertTrue(0x3005.isKana())
    }

    @Test
    fun `maru is a kana`() {
        assertTrue(0x3007.isKana())
    }

    @Test
    fun `random character is not a kana`() {
        assertFalse(0x29A.isKana())
    }

    @Test
    fun `first kanji is a japanese-chinese character`() {
        assertTrue(0x4E00.isKanji())
    }

    @Test
    fun `last kanji is a japanese-chinese character`() {
        assertTrue(0x9FAF.isKanji())
    }

    @Test
    fun `random character is not a japanese character`() {
        assertFalse(0xFB44.isKana())
        assertFalse(0xFB44.isKanji())
    }

    @Test
    fun `check contains roomaji`() {
        assertEquals(JapaneseCodePoints.None, "hello".japaneseCodePoints())
    }

    @Test
    fun `check contains kana`() {
        assertEquals(JapaneseCodePoints.OnlyKana, "こんにち".japaneseCodePoints())
    }

    @Test
    fun `check contains kanji`() {
        assertEquals(JapaneseCodePoints.OnlyKanjiAndKana, "今日".japaneseCodePoints())
    }

    @Test
    fun `check contains kana and kanji`() {
        assertEquals(JapaneseCodePoints.OnlyKanjiAndKana, "言う".japaneseCodePoints())
    }

    @Test
    fun `check mix`() {
        assertEquals(JapaneseCodePoints.Some, "today 今日".japaneseCodePoints())
    }
}