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
data class Krad(val kanji: Char, val radicals: List<Char>)

fun decodeKrad(source: BufferedSource): List<Krad> {
    val lexer = RadicalLexer(source)
    val decoder = KradDecoder(lexer)
    return decoder.decodeSerializableValue(ListSerializer(Krad.serializer()))
}

private const val DELIMITER = ':'.code

@OptIn(ExperimentalSerializationApi::class)
private class KradDecoder(
    private val lexer: RadicalLexer,
) : AbstractDecoder() {
    private var elementIndex = 0

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        lexer.skipComments()
        if (descriptor.kind == StructureKind.LIST) {
            if (descriptor.elementDescriptors.any { it.kind is PrimitiveKind }) {
                lexer.skipWhitespaces()
                lexer.consumeNextToken(DELIMITER)
            }
            return KradDecoder(lexer)
        }
        return KradDecoder(lexer)
    }

    override fun decodeChar(): Char {
        lexer.skipWhitespaces()
        return lexer.consumeChar()
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        return when (descriptor.kind) {
            StructureKind.LIST, StructureKind.MAP -> decodeCollectionIndex(descriptor)
            else -> decodeObjectIndex(descriptor)
        }
    }

    private fun decodeCollectionIndex(descriptor: SerialDescriptor): Int {
        val isPrimitive = descriptor.elementDescriptors.any { it.kind is PrimitiveKind }
        if (!isPrimitive) lexer.skipWhitespaces()
        if (lexer.isExhausted() || (isPrimitive && lexer.peek().isNewline())) {
            return CompositeDecoder.DECODE_DONE
        }
        return elementIndex++
    }

    private fun decodeObjectIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        return elementIndex++
    }
}
