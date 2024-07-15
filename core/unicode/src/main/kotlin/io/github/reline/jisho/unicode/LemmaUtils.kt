/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package io.github.reline.jisho.unicode

import org.atilika.kuromoji.Token
import org.atilika.kuromoji.Tokenizer
import ve.Parse
import ve.Word

internal fun String.tokenize(): List<Word> {
    // Output the Kuromoji-style Tokens as a List
    val tokensList: List<Token> = Tokenizer.builder().build().tokenize(this)
    // Convert to a basic Token array
    val tokensArray: Array<Token> = tokensList.toTypedArray()
    // Create a parser instance from the array of Kuromoji-style Tokens.
    val parser = Parse(tokensArray)
    // Get the Tokens out as 'Words'.
    return parser.words()
}

fun String.asLemmas(): String = tokenize().joinToString(separator = " ") { it.lemma }
