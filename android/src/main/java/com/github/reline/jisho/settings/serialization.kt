package com.github.reline.jisho.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.squareup.wire.Message
import okio.IOException
import java.io.InputStream
import java.io.OutputStream

fun <M : Message<M, B>, B : Message.Builder<M, B>> wireDataStore(
    fileName: String,
    defaultValue: M,
) = dataStore(
    fileName,
    ProtoSerializer(defaultValue),
)

class ProtoSerializer<M : Message<M, B>, B : Message.Builder<M, B>>(
    override val defaultValue: M,
) : Serializer<M> {
    private val adapter = defaultValue.adapter
    override suspend fun readFrom(input: InputStream) = try {
        adapter.decode(input)
    } catch (e: IOException) {
        throw CorruptionException("Cannot read proto.", e)
    }

    override suspend fun writeTo(t: M, output: OutputStream) = adapter.encode(output, t)
}
