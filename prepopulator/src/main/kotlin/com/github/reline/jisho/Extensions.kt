/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import okio.BufferedSource
import okio.ByteString.Companion.decodeHex
import okio.IOException
import okio.Options

/** Byte order marks. */
private val UNICODE_BOMS = Options.of(
        "efbbbf".decodeHex(), // UTF-8
        "feff".decodeHex(), // UTF-16BE
        "fffe".decodeHex(), // UTF-16LE
        "0000ffff".decodeHex(), // UTF-32BE
        "ffff0000".decodeHex() // UTF-32LE
)

@Throws(IOException::class)
fun BufferedSource.skipBom() {
    select(UNICODE_BOMS)
}