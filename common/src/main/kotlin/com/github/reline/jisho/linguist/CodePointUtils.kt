/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.linguist

/**
 * http://www.rikai.com/library/kanjitables/kanji_codes.unicode.shtml
 * http://www.tamasoft.co.jp/en/general-info/unicode.html
 *
 * UNICODE RANGE : DESCRIPTION
 * 3000-303F : punctuation
 * 3040-309F : hiragana
 * 30A0-30FF : katakana
 * FF00-FFEF : Full-width roman + half-width katakana
 * 4E00-9FAF : Common and uncommon kanji
 *
 * The characters from 0x4e00 to 0x9faf include Chinese-only characters. There are 6355 standard Japanese kanji.
 * Sort them by char code (e.g. ja-JP) and they should be ordered as such (followed by Chinese characters):
 * JIS X 0208 - Level 1 Kanji (2965 characters)
 * JIS X 0208 - Level 2 Kanji (3390 characters)
 *
 * Non-Japanese punctuation/formatting characters commonly used in Japanese text
 * 2605-2606 : Stars
 * 2190-2195 : Arrows
 * 203B      : Reference mark ※
 */

private const val PUNCTUATION_STARTING_CODEPOINT = 0x3000
private const val KATAKANA_END_CODEPOINT = 0x30FF
private const val CJK_STARTING_CODEPOINT = 0x4E00
private const val CJK_ENDING_CODEPOINT = 0x9FAF

data class Result(
        // really this just means that at least one character is not a kana and not a kanji
        val containsRoomaji: Boolean,
        // HIRAGANA_STARTING_CODEPOINT..KATAKANA_END_CODEPOINT
        val containsKana: Boolean,
        // CJK_STARTING_CODEPOINT..CJK_ENDING_CODEPOINT
        val containsKanji: Boolean
)

/**
 * Check if a word contains roomaji, kana, or kanji in a single pass
 */
fun checkCJK(s: String): Result {
    var containsRoomaji = false
    var containsKana = false
    var containsKanji = false
    s.forEach {
        val c = it.code
        containsKana = containsKana || isKana(c)
        containsKanji = containsKanji || isCJK(c)
        val containsJapanese = containsKana || containsKanji
        // if it's not japanese, then we'll just consider it as roomaji. good 'nuff.
        containsRoomaji = containsRoomaji || !containsJapanese
        // if we already know this word has both, then return early
        if (containsRoomaji && containsJapanese) return@forEach
    }
    return Result(
            containsRoomaji = containsRoomaji,
            containsKana = containsKana,
            containsKanji = containsKanji
    )
}

/**
 * @param c A codepoint
 * @return True if the codepoint is a kana character
 */
fun isKana(c: Int): Boolean {
    return c in PUNCTUATION_STARTING_CODEPOINT..KATAKANA_END_CODEPOINT
}

/**
 * @param c A codepoint
 * @return True if the codepoint is a japanese, chinese, or korean character
 * todo: look at https://github.com/KanjiVG/kanjivg/blob/3d5b57d2b178265744dc8bcb364930c9247f5a0e/kanjivg.py#L37
 */
fun isCJK(c: Int): Boolean {
    return c in CJK_STARTING_CODEPOINT..CJK_ENDING_CODEPOINT
}
