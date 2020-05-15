package com.github.reline.jisho

import com.github.reline.jisho.dictmodels.kanji.KanjiDictionary
import com.tickaroo.tikxml.TikXml
import okio.Buffer
import java.io.File

fun main() = runKanji()

fun runKanji() {
    println("Extracting kanji...")
    extractKanji(arrayOf(
            File("$buildDir/dict/kanjidic2.xml")
    ))
}

private fun extractKanji(files: Array<File>) {
    val dictionaries = files.map { file ->
        val inputStream = file.inputStream()
        val source = Buffer().readFrom(inputStream)

        val parseStart = System.currentTimeMillis()

        val dictionary: KanjiDictionary = TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
                .read(source, KanjiDictionary::class.java)
        source.clear()
        inputStream.close()

        val parseEnd = System.currentTimeMillis()

        println("${file.name}: Parsing ${dictionary.characters?.size} kanji took ${(parseEnd - parseStart)}ms")

        return@map dictionary
    }

    dictionaries.forEachIndexed { i, dictionary ->
        val start = System.currentTimeMillis()
        println("Inserting ${files[i].name} to database...")
        insertKanji(dictionary)
        val end = System.currentTimeMillis()
        println("${files[i].name}: Inserting ${dictionary.characters?.size} kanji took ${(end - start)}ms")
    }
}

private fun insertKanji(dictionary: KanjiDictionary) = with(database) {
    transaction {
        dictionary.characters?.forEach {
//            kanjiRadicalQueries.insertKanji(it.literal)
        }
    }
}