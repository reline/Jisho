package com.github.reline.jishodb

import com.github.reline.jishodb.dictmodels.jmdict.Dictionary
import com.github.reline.jishodb.dictmodels.jmdict.Entry
import com.tickaroo.tikxml.TikXml
import kotlinx.coroutines.runBlocking
import okio.Buffer
import java.io.File

fun main() = runDictionaries()

fun runDictionaries() = runBlocking {
    logger.info("Extracting dictionaries...")
    extractDictionaries(arrayOf(
            File("jishodb/build/dict/JMdict_e.xml")
//            File("jishodb/build/dict/JMnedict.xml")
    ))
}

private suspend fun extractDictionaries(files: Array<File>) {
    files.forEach { file ->
        val inputStream = file.inputStream()
        val source = Buffer().readFrom(inputStream)

        val parseStart = System.currentTimeMillis()

        val dictionary: Dictionary = TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
                .read(source, Dictionary::class.java)
        source.clear()
        inputStream.close()

        val parseEnd = System.currentTimeMillis()

        logger.info("${file.name}: Parsing ${dictionary.entries.size} entries took ${(parseEnd - parseStart)}ms")

        val start = System.currentTimeMillis()
        logger.info("Inserting ${file.name} to database...")

        insertDictionary(dictionary.entries)
        val end = System.currentTimeMillis()
        logger.info("${file.name}: Inserting ${dictionary.entries.size} entries took ${(end - start)}ms")
    }
}

private fun insertEntries(entries: List<Entry>) = with(database) {
    logger.info("Inserting Entries...")
    transaction {
        entries.forEach { entry ->
            val kanji = entry.kanji?.firstOrNull()?.value
            val reading = entry.readings.first().value
            entryQueries.insert(entry.id, entry.isCommon(), kanji, reading)
        }
    }
}

private fun insertGlosses(entries: List<Entry>) = with(database) {
    logger.info("Inserting Glosses...")
    transaction {
        entries.forEach { entry ->
            entry.senses.forEach { sense ->
                sense.glosses?.forEach { gloss ->
                    glossQueries.insert(gloss.value)
                }
            }
        }
    }
}

private fun insertPartsOfSpeech(entries: List<Entry>) = with(database) {
    logger.info("Inserting Parts of Speech...")
    transaction {
        entries.forEach { entry ->
            entry.senses.forEach { sense ->
                sense.partsOfSpeech?.forEach { pos ->
                    partOfSpeechQueries.insert(pos.decoded())
                }
            }
        }
    }
}

private fun insertSenses(entries: List<Entry>) = with(database) {
    val list = arrayListOf<Long>()

    logger.info("Inserting Senses...")
    transaction {
        entries.forEach { entry ->
            entry.senses.forEach {
                senseQueries.insert(entry.id)
                list.add(utilQueries.lastInsertRowId().executeAsOne())
            }
        }
    }

    var iter = list.iterator()

    logger.info("  SenseGlossTag")
    transaction {
        entries.forEach { entry ->
            entry.senses.forEach { sense ->
                val senseId = iter.next()
                sense.glosses?.forEach { gloss ->
                    val glossId = glossQueries.selectGlossIdWhereValueEquals(gloss.value).executeAsOne()
                    senseGlossTagQueries.insert(senseId, glossId)
                }
            }
        }
    }

    iter = list.iterator()

    logger.info("  SensePosTag")
    transaction {
        entries.forEach { entry ->
            entry.senses.forEach { sense ->
                val senseId = iter.next()
                sense.partsOfSpeech?.forEach { pos ->
                    sensePosTagQueries.insert(
                            senseId,
                            partOfSpeechQueries.selectPosIdWhereValueEquals(pos.decoded())
                                    .executeAsOne()
                    )
                }
            }
        }
    }
}

private suspend fun insertDictionary(entries: List<Entry>) = with(database) {
    insertEntries(entries)
    gc()
    insertGlosses(entries)
    gc()
    insertPartsOfSpeech(entries)
    gc()
    insertSenses(entries)
}