/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.dictmodels

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import okio.BufferedSource

@Serializable
data class Radk(val radical: Char, val strokes: Int, val kanji: List<Char>)

fun decodeRadicals(source: BufferedSource): List<Radk> {
    val lexer = RadicalLexer(source)
    val decoder = RadkDecoder(lexer)
    return decoder.decodeSerializableValue(ListSerializer(Radk.serializer()))
}

private const val BEGIN_OBJ = '$'.code

@OptIn(ExperimentalSerializationApi::class)
private class RadkDecoder(
    private val lexer: RadicalLexer,
) : AbstractDecoder() {
    private var elementIndex = 0

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        lexer.skipComments()
        if (descriptor.kind == StructureKind.LIST) {
            lexer.skipWhitespaces()
            return RadkDecoder(lexer)
        }
        lexer.consumeNextToken(BEGIN_OBJ)
        return RadkDecoder(lexer)
    }

    override fun decodeChar(): Char {
        lexer.skipWhitespaces()
        return lexer.consumeChar()
    }

    override fun decodeInt(): Int {
        lexer.skipWhitespaces()
        return lexer.consumeInt()
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        return when (descriptor.kind) {
            StructureKind.LIST, StructureKind.MAP -> decodeCollectionIndex(descriptor)
            else -> decodeObjectIndex(descriptor)
        }
    }

    private fun decodeCollectionIndex(descriptor: SerialDescriptor): Int {
        val isPrimitive = descriptor.elementDescriptors.any { it.kind is PrimitiveKind }
        lexer.skipWhitespaces()
        if (lexer.isExhausted() || isPrimitive == (lexer.peek() == BEGIN_OBJ)) {
            return CompositeDecoder.DECODE_DONE
        }
        return elementIndex++
    }

    private fun decodeObjectIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        return elementIndex++
    }
}
