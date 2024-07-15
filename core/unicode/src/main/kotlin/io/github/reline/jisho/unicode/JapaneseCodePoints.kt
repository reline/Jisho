/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package io.github.reline.jisho.unicode

/**
 * http://www.rikai.com/library/kanjitables/kanji_codes.unicode.shtml
 * http://www.tamasoft.co.jp/en/general-info/unicode.html
 * https://www.unicode.org/charts/
 *
 * There are 6355 standard Japanese kanji. Sort them by char code (e.g. ja-JP) and they should be
 * ordered as such (followed by Chinese characters):
 * JIS X 0208 - Level 1 Kanji (2965 characters)
 * JIS X 0208 - Level 2 Kanji (3390 characters)
 *
 * Non-Japanese punctuation/formatting characters commonly used in Japanese text
 * 2605-2606 : Stars
 * 2190-2195 : Arrows
 * 203B      : Reference mark ※
 */

// https://www.unicode.org/charts/PDF/U2E80.pdf
private val RADICALS_SUPPLEMENT = 0x2E80..0x2EEF

// 0x2FD6..0x2FDF are omitted since they are reserved.
// https://www.unicode.org/charts/PDF/U2F00.pdf
private val RADICALS = 0x2F00..0x2FD5

// CJK Symbols and Punctuation
// todo: consider omitting some of these.
// https://www.unicode.org/charts/PDF/U3000.pdf
private val CJK_SYMBOLS_PUNCTUATION = 0x3000..0x303F

// https://www.unicode.org/charts/PDF/U3040.pdf
private val HIRAGANA = 0x3040..0x309F
// https://www.unicode.org/charts/PDF/U30A0.pdf
private val KATAKANA_FULL_WIDTH = 0x30A0..0x30FF
// https://www.unicode.org/charts/PDF/U31F0.pdf
private val KATAKANA_PHONETIC = 0x31F0..0x31FF

// Enclosed CJK Letters and Months
// Only japanese is included.
// https://www.unicode.org/charts/PDF/U3200.pdf
private val ENCLOSED_JAPANESE_LETTERS_MONTHS = listOf(
    // parenthesized ideographs
    0x3220..0x3243,

    // circled ideographs, circled numbers on black squares, squared latin abbr., circled numbers
    // todo: include 0x3244..0x325F?

    // circled ideographs, circled numbers, telegraph symbols for months, squared latin abbr.,
    // circled katakana, japanese era name
    0x3280..0x32FF,
)

// CJK Compatibility
// https://www.unicode.org/charts/PDF/U3300.pdf
private val COMPATIBILITY = 0x3300..0x33FF

// CJK unified ideographs Extension A
// https://www.unicode.org/charts/PDF/U3400.pdf
private val KANJI_RARE = 0x3400..0x4DBF

// CJK unifed ideographs
// https://www.unicode.org/charts/PDF/U4E00.pdf
private val KANJI = 0x4E00..0x9FFF

// CJK compatibility ideographs
// https://www.unicode.org/charts/PDF/UF900.pdf
private val KANJI_COMPAT = 0xF900..0xFAFF

// Halfwidth and Fullwidth Forms
// Only japanese is included.
// https://www.unicode.org/charts/PDF/UFF00.pdf
private val WIDTH_FORMS = listOf(
    // ASCII variants, fullwidth brackets, halfwidth punctuation, katakana variants
    0xFF00..0xFF9F,
    // symbol variants
    0xFFE0..0XFFEE,
)

sealed interface JapaneseCodePoints {
    data object OnlyKanjiAndKana : JapaneseCodePoints
    data object OnlyKana : JapaneseCodePoints
    data object Some : JapaneseCodePoints
    data object None : JapaneseCodePoints
}

fun String.japaneseCodePoints(): JapaneseCodePoints {
    var containsKanji = false
    var containsKana = false
    var containsOther = false

    for (char in this) {
        val isKana = char.isKana()
        val isKanji = char.isKanji()
        val isJapanese = isKana || isKanji
        containsKana = containsKana || isKana
        containsKanji = containsKanji || isKanji
        containsOther = containsOther || !isJapanese
        if (containsOther && (containsKana || containsKanji)) return JapaneseCodePoints.Some
    }

    return when {
        containsKanji -> JapaneseCodePoints.OnlyKanjiAndKana
        containsKana -> JapaneseCodePoints.OnlyKana
        else -> JapaneseCodePoints.None
    }
}

fun Char.isKana(): Boolean = code.isKana()

fun Int.isKana(): Boolean {
    return this in CJK_SYMBOLS_PUNCTUATION ||
            this in HIRAGANA ||
            this in KATAKANA_FULL_WIDTH ||
            WIDTH_FORMS.any { this in it }
}

fun Char.isKanji(): Boolean = code.isKanji()

fun Int.isKanji(): Boolean {
    return this in KANJI ||
            this in KANJI_COMPAT ||
            this in KANJI_RARE
}
