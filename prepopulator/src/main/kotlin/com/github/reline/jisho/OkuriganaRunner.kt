package com.github.reline.jisho

import com.github.reline.jisho.dictmodels.okurigana.OkuriganaEntry
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.Buffer
import java.io.File
import java.lang.reflect.Type

fun main() = runOkurigana()

fun runOkurigana() {
    println("Extracting okurigana...")
    extractOkurigana(arrayOf(
            File("$buildDir/dict/JmdictFurigana.json")
//            File("$buildDir/dict/JmnedictFurigana.json")
    ))
}

/**
 * See okurigana.json
 */
private fun extractOkurigana(files: Array<File>) {
    val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    val type: Type = Types.newParameterizedType(List::class.java, OkuriganaEntry::class.java)
    val adapter: JsonAdapter<List<OkuriganaEntry>> = moshi.adapter(type)

    val entries = files.map { file ->
        val inputStream = file.inputStream()

        val source = Buffer().readFrom(inputStream)
        source.skipBom()

        val parseStart = System.currentTimeMillis()

        println("Parsing ${file.name}...")

        val entries = try {
            adapter.fromJson(source) ?: throw IllegalStateException("Entries were null")
        } finally {
            source.clear()
            inputStream.close()
        }

        val parseEnd = System.currentTimeMillis()

        println("${file.name}: Parsing ${entries.size} okurigana took ${(parseEnd - parseStart)}ms")

        return@map entries
    }

    entries.forEach {
        insertOkurigana(it)
    }
}

private fun insertOkurigana(okurigana: List<OkuriganaEntry>) = with(database) {
    logger.info("Inserting okurigana...")
    transaction {
        okurigana.forEach { ruby ->
            ruby.furigana.forEach { furigana ->
                rubyQueries.insert(furigana.ruby, furigana.rt)
            }
        }
    }

    logger.info("Inserting okurigana tags...")
    transaction {
        okurigana.forEach { ruby ->
            entryQueries.selectEntryIdWhereValuesEqual(ruby.text, ruby.reading).executeAsList().forEach { entryId ->
                ruby.furigana.forEachIndexed { position, furigana ->
                    val rubyId = rubyQueries.selectRubyId(furigana.ruby, furigana.rt).executeAsOne()
                    entryRubyTagQueries.insert(entryId, rubyId, position.toLong())
                }
            }
        }
    }
}