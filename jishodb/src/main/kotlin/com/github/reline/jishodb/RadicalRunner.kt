package com.github.reline.jishodb

import com.github.reline.jishodb.dictmodels.Radical
import java.io.File

fun main() = runRadicals()

fun runRadicals() {
    println("Extracting radicals...")
    extractRadKFiles(arrayOf(
            File("jishodb/build/dict/radkfile"),
            File("jishodb/build/dict/radkfile2"),
            File("jishodb/build/dict/radkfilex")
    ))

    extractKRadFiles(arrayOf(
            File("jishodb/build/dict/kradfile"),
            File("jishodb/build/dict/kradfile2")
    ))
}

private fun extractRadKFiles(files: Array<File>) {
    val radkparser = RadKParser()
    files.forEach {
        val parseStart = System.currentTimeMillis()
        val radicals = radkparser.parse(it)
        val parseEnd = System.currentTimeMillis()
        println("${it.name}: Parsing took ${(parseEnd - parseStart)}ms")

        val insertStart = System.currentTimeMillis()
        insertRadk(radicals)
        val insertEnd = System.currentTimeMillis()
        println("${it.name}: Inserting took ${(insertEnd - insertStart)}ms")
    }
}

private fun insertRadk(radicals: List<Radical>) = with(database) {
    transaction(noEnclosing = true) {
        radicals.forEach { radical ->
            kanjiRadicalQueries.insertRadical(radical.value.toString(), radical.strokes.toLong())
            val radicalId = kanjiRadicalQueries.radicalId(radical.value.toString()).executeAsOne()
            radical.kanji.forEach { kanji ->
                kanjiRadicalQueries.insertKanji(kanji.toString())
                val kanjiId = kanjiRadicalQueries.kanjiId(kanji.toString()).executeAsOne()
                kanjiRadicalQueries.insertKanjiRadicalTag(kanjiId, radicalId)
            }
        }
    }
}

fun extractKRadFiles(files: Array<File>) {
    val kradparser = KRadParser()
    files.forEach {
        val parseStart = System.currentTimeMillis()
        val krad = kradparser.parse(it)
        val parseEnd = System.currentTimeMillis()
        println("${it.name}: Parsing took ${(parseEnd - parseStart)}ms")

        val insertStart = System.currentTimeMillis()
        insertKrad(krad)
        val insertEnd = System.currentTimeMillis()
        println("${it.name}: Inserting took ${(insertEnd - insertStart)}ms")
    }
}

fun insertKrad(krad: Map<Char, List<Char>>) = with(database) {
    transaction(noEnclosing = true) {
        krad.forEach { (kanji, radicals) ->
            kanjiRadicalQueries.insertKanji(kanji.toString())
            val kanjiId = kanjiRadicalQueries.kanjiId(kanji.toString()).executeAsOne()
            radicals.forEach { radical ->
                try {
                    val radicalId = kanjiRadicalQueries.radicalId(radical.toString()).executeAsOne()
                    kanjiRadicalQueries.insertKanjiRadicalTag(kanjiId, radicalId)
                } catch (e: NullPointerException) {
                    // note: 悒 contains the radical 邑 which isn't in the radfile
                    // but 悒 explicitly consists of 口 and 巴 which make up 邑

                    // omit any failures for now
                    println("$radical couldn't be found, used in $kanji")
                }
            }
        }
    }
}