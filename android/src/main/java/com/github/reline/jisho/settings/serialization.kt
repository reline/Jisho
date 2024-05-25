package com.github.reline.jisho.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.squareup.wire.ProtoAdapter
import okio.IOException
import java.io.InputStream
import java.io.OutputStream

internal fun <T> ProtoAdapter<T>.asSerializer(defaultValue: T) = object : Serializer<T> {
    override val defaultValue = defaultValue

    override suspend fun readFrom(input: InputStream) = try {
        decode(input)
    } catch (e: IOException) {
        throw CorruptionException("Cannot read proto.", e)
    }

    override suspend fun writeTo(t: T, output: OutputStream) = encode(output, t)
}
