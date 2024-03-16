package com.github.reline.jisho

import okio.BufferedSource
import okio.ByteString.Companion.decodeHex
import okio.FileSystem
import okio.IOException
import okio.Options
import okio.Path

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

// todo: unit test; does this empty the contents of an existing file?
fun FileSystem.touch(path: Path) {
    path.parent?.let { createDirectories(it) }
    write(path) {}
}
