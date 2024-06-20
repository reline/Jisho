package com.github.reline.jisho.dictmodels

import kotlinx.serialization.SerializationException
import okio.Buffer
import okio.BufferedSource

private const val COMMENT = '#'.code

internal class RadicalLexer(private val source: BufferedSource) {
    fun skipComments() {
        while (!isExhausted() && peek() == COMMENT) {
            source.readUtf8Line()
        }
    }

    fun skipWhitespaces() {
        while (!isExhausted() && peek().toChar().isWhitespace()) {
            source.readUtf8CodePoint()
        }
    }

    fun isExhausted(): Boolean {
        return source.exhausted()
    }

    fun consumeNextToken(token: Int) {
        val codePoint = source.readUtf8CodePoint()
        if (codePoint != token) throw SerializationException("Unexpected token")
    }

    fun peek(): Int {
        return source.peek().readUtf8CodePoint()
    }

    fun consumeChar(): Char {
        return source.readUtf8CodePoint().toChar()
    }

    fun consumeInt(): Int {
        val buffer = Buffer()
        while (peek().toChar().isDigit()) {
            buffer.writeUtf8CodePoint(source.readUtf8CodePoint())
        }
        return buffer.readUtf8().toInt()
    }
}

fun Int.isNewline() = this == '\n'.code || this == '\r'.code
