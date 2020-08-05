/*
* Copyright 2020 Nathaniel Reline
*
* This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
* To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
* send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
*/

package com.github.reline.jisho.linguist

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CodePointUtilsTest {
    @Test
    fun `first hiragana should be a kana`() {
        assertTrue(isKana(0x3040))
    }

    @Test
    fun `last katakana should be a kana`() {
        assertTrue(isKana(0x30FF))
    }

    @Test
    fun `random kana is kana`() {
        assertTrue(isKana(0x3092))
    }

    @Test
    fun `ditto mark is a kana`() {
        assertTrue(isKana(0x3005))
    }

    @Test
    fun `maru is a kana`() {
        assertTrue(isKana(0x3007))
    }

    @Test
    fun `random character is not a kana`() {
        assertFalse(isKana(0x29A))
    }

    @Test
    fun `first kanji is a japanese-chinese character`() {
        assertTrue(isCJK(0x4E00))
    }

    @Test
    fun `last kanji is a japanese-chinese character`() {
        assertTrue(isCJK(0x9FAF))
    }

    @Test
    fun `random character is not a kanji`() {
        assertFalse(isCJK(0xFB44))
    }
}