/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.linguist

import kotlin.test.Test
import kotlin.test.assertEquals

class TokenizeTest {
    @Test
    fun test() {
        // Some nonsense Japanese with interesting word boundaries.
        val stringOfJapanese = "お金がなければいけないです。"
        // The .toString() method of each Word is generally the most useful. It shows the surface form of the Tokens.
        assertEquals(arrayListOf("お金", "が", "なければいけない", "です", "。"), stringOfJapanese.tokenize().map { it.toString() })
    }

    @Test
    fun lemmaTest() {
        val actual = "走った".tokenize().joinToString(separator = " ") { it.lemma }
        assertEquals("走る", actual)
    }

    @Test
    fun lemmas() {
        val actual = "走った".asLemmas()
        assertEquals("走る", actual)
    }
}