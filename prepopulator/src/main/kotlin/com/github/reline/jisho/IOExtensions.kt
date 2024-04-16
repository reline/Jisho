package com.github.reline.jisho

import okio.BufferedSource
import okio.ByteString.Companion.decodeHex
import okio.IOException
import okio.Options
import java.io.File

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

fun File.touch() {
    parentFile.mkdirs()
    createNewFile()
}

fun requireFile(file: File) {
    require(file.exists()) { "file does not exist: $file" }
    require(file.isFile) { "not a file: $file" }
}
