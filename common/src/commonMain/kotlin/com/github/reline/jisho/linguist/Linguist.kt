/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.linguist

data class Result(
        // really this just means that at least one character is not a kana and not a kanji
        val containsRoomaji: Boolean,
        // HIRAGANA_STARTING_CODEPOINT..KATAKANA_END_CODEPOINT
        val containsKana: Boolean,
        // CJK_STARTING_CODEPOINT..CJK_ENDING_CODEPOINT
        val containsKanji: Boolean
)