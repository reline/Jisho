package com.github.reline.jishodb

import com.github.reline.jishodb.dictmodels.jmdict.Dictionary
import com.github.reline.jishodb.dictmodels.jmdict.Entry
import com.tickaroo.tikxml.TikXml
import okio.Buffer
import java.io.File

fun main() = runDictionaries()

fun runDictionaries() {
    logger.info("Extracting dictionaries...")
    extractDictionaries(arrayOf(
            File("jishodb/build/dict/JMdict_e.xml")
//            File("jishodb/build/dict/JMnedict.xml")
    ))
}

private fun extractDictionaries(files: Array<File>) {
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
            entryQueries.insertEntry(entry.id, entry.isCommon())
        }
    }
}

private fun insertKanji(entries: List<Entry>) = with(database) {
    logger.info("Inserting Kanji...")
    transaction {
        entries.forEach { entry ->
            entry.kanji?.forEach { kanji ->
                kanjiElementQueries.insertKanji(kanji.value, kanji.isCommon())
                val kanjiId = kanjiElementQueries.rowid().executeAsOne()
                entryQueries.insertEntryKanjiTag(entry.id, kanjiId)
            }
        }
    }
}

private fun insertReadings(entries: List<Entry>) = with(database) {
    logger.info("Inserting Readings...")
    transaction {
        entries.forEach { entry ->
            entry.readings.forEach { reading ->
                readingQueries.insertReading(reading.value, reading.isNotTrueReading(), reading.isCommon())
                val readingId = readingQueries.rowid().executeAsOne()
                entryQueries.insertEntryReadingTag(entry.id, readingId)
            }
        }
    }
}

private fun insertSenses(entries: List<Entry>) = with(database) {
    logger.info("Inserting Senses...")
//     Kanji and Reading tags force a dependency of Sense on Kanji & Reading
    transaction {
        entries.forEach { entry ->
            entry.senses.forEachIndexed { i, _ ->
                val senseId = "${entry.id}$i".toLong()
                senseQueries.insertSense(senseId)
                senseQueries.rowid().executeAsOne()
            }
        }
    }

    transaction {
        entries.forEach { entry ->
            entry.senses.forEachIndexed { i, _ ->
                val senseId = "${entry.id}$i".toLong()
                entryQueries.insertEntrySenseTag(entry.id, senseId)
            }
        }
    }

    logger.info("Inserting Parts of Speech...")
    transaction {
        entries.forEach { entry ->
            entry.senses.forEachIndexed { i, sense ->
                sense.partsOfSpeech?.forEach {
                    partOfSpeechQueries.insertPartOfSpeech(it.value)
                    val posId = partOfSpeechQueries.rowid(it.value).executeAsOne()
                    val senseId = "${entry.id}$i".toLong()
                    senseQueries.insertSensePosTag(senseId, posId)
                }
            }
        }
    }

    logger.info("Inserting Glosses...")
    transaction {
        entries.forEach { entry ->
            entry.senses.forEachIndexed { i, sense ->
                sense.glosses?.forEach { gloss ->
                    glossQueries.insertGloss(gloss.value, gloss.language, gloss.gender)
                    val glossId = glossQueries.rowid().executeAsOne()
                    val senseId = "${entry.id}$i".toLong()
                    senseQueries.insertSenseGlossTag(senseId, glossId)
                }
            }
        }
    }
}

private fun insertDictionary(entries: List<Entry>) = with(database) {
    insertEntries(entries)
    insertKanji(entries)

//    transaction {
//        entries.forEach { entry ->
//            entry.kanji?.forEach { kanji ->
//                val kanjiId = kanjiElementQueries.selectKanjiByValue(kanji.value).executeAsOne()
//                kanji.information?.forEach { info ->
//                    infoQueries.insertInfo(info.value, info.getInfo())
//                    val infoId = infoQueries.rowid(info.value).executeAsOne()
//                    kanjiElementQueries.insertKanjiInfoTag(kanjiId, infoId)
//                }
//            }
//        }
//    }

//    transaction {
//        entries.forEach { entry ->
//            entry.kanji?.forEach { kanji ->
//                val kanjiId = kanjiElementQueries.selectKanjiByValue(kanji.value).executeAsOne()
//                kanji.priorities?.forEach { priority ->
//                    priorityQueries.insertPriority(priority.value, priority.isCommon())
//                    val priorityId = priorityQueries.rowid(priority.value).executeAsOne()
//                    kanjiElementQueries.insertKanjiPriorityTag(kanjiId, priorityId)
//                }
//            }
//        }
//    }

    // Reading Restriction forces a dependency of Reading on Kanji
    insertReadings(entries)

//    logger.info("Inserting Reading restrictions...")
//    transaction {
//        entries.forEach { entry ->
//            entry.readings.forEach { reading ->
//                val readingId = readingQueries.selectReadingByValue(reading.value).executeAsOne()
//                reading.restrictions?.forEach { restriction ->
//                    readingQueries.insertReadingRestrictionTag(readingId, restriction.kanji)
//                }
//            }
//        }
//    }

//    logger.info("Inserting Reading info...")
//    transaction {
//        entries.forEach { entry ->
//            entry.readings.forEach { reading ->
//                val readingId = readingQueries.selectReadingByValue(reading.value).executeAsOne()
//                reading.information?.forEach { info ->
//                    infoQueries.insertInfo(info.value, info.info)
//                    val infoId = infoQueries.rowid(info.value).executeAsOne()
//                    readingQueries.insertReadingInfoTag(readingId, infoId)
//                }
//            }
//        }
//    }

//    logger.info("Inserting Reading priority...")
//    transaction {
//        entries.forEach { entry ->
//            entry.readings.forEach { reading ->
//                val readingId = readingQueries.selectReadingByValue(reading.value).executeAsOne()
//                reading.priorities?.forEach { priority ->
//                    priorityQueries.insertPriority(priority.value, priority.isCommon())
//                    val priorityId = priorityQueries.rowid(priority.value).executeAsOne()
//                    readingQueries.insertReadingPriorityTag(readingId, priorityId)
//                }
//            }
//        }
//    }

    insertSenses(entries)

//    transaction {
//        senseIds.keys.forEach { entry ->
//            senseIds[entry]?.forEach { (sense, senseId) ->
//                sense.kanjiTags?.forEach {
//                    senseQueries.insertSenseKanjiTag(senseId, it.value)
//                }
//            }
//        }
//    }

//    transaction {
//        senseIds.keys.forEach { entry ->
//            senseIds[entry]?.forEach { (sense, senseId) ->
//                sense.readingTags?.forEach {
//                    senseQueries.insertSenseReadingTag(senseId, it.value)
//                }
//            }
//        }
//    }

    // pos

//    transaction {
//        senseIds.keys.forEach { entry ->
//            senseIds[entry]?.forEach { (sense, senseId) ->
//                sense.seeAlso?.forEach {
//                    xReferenceQueries.insertXReference(it.value)
//                    val xrefId = xReferenceQueries.rowid().executeAsOne()
//                    senseQueries.insertSenseXRefTag(senseId, xrefId)
//                }
//            }
//        }
//    }

//    transaction {
//        senseIds.keys.forEach { entry ->
//            senseIds[entry]?.forEach { (sense, senseId) ->
//                sense.antonyms?.forEach { antonym ->
//                    antonymQueries.insertAntonym(senseId, antonym.value)
//                }
//            }
//        }
//    }

//    transaction {
//        senseIds.keys.forEach { entry ->
//            senseIds[entry]?.forEach { (sense, senseId) ->
//                sense.fields?.forEach {
//                    fieldQueries.insertField(it.value)
//                    val fieldId = fieldQueries.rowid(it.value).executeAsOne()
//                    senseQueries.insertSenseFieldTag(senseId, fieldId)
//                }
//            }
//        }
//    }

//    transaction {
//        senseIds.keys.forEach { entry ->
//            senseIds[entry]?.forEach { (sense, senseId) ->
//                sense.miscellaneous?.forEach {
//                    infoQueries.insertInfo(it.value, it.getTag())
//                    val miscId = infoQueries.rowid(it.value).executeAsOne()
//                    senseQueries.insertSenseMiscTag(senseId, miscId)
//                }
//            }
//        }
//    }

//    transaction {
//        senseIds.keys.forEach { entry ->
//            senseIds[entry]?.forEach { (sense, senseId) ->
//                sense.information?.forEach {
//                    senseInfoQueries.insertSenseInfo(it.value)
//                    val infoId = senseInfoQueries.rowid().executeAsOne()
//                    senseQueries.insertSenseInfoTag(senseId, infoId)
//                }
//            }
//        }
//    }

//    transaction {
//        senseIds.keys.forEach { entry ->
//            senseIds[entry]?.forEach { (sense, senseId) ->
//                sense.sources?.forEach {
//                    sourceQueries.insertSource(it.value, it.language, it.isFullDescription(), it.isWaseieigo())
//                    val sourceId = sourceQueries.rowid().executeAsOne()
//                    senseQueries.insertSenseSourceTag(senseId, sourceId)
//                }
//            }
//        }
//    }

//    transaction {
//        senseIds.keys.forEach { entry ->
//            senseIds[entry]?.forEach { (sense, senseId) ->
//                sense.dialects?.forEach { dialect ->
//                    dialectQueries.insertDialect(dialect.value)
//                    val dialectId = dialectQueries.rowid(dialect.value).executeAsOne()
//                    senseQueries.insertSenseDialectTag(senseId, dialectId)
//                }
//            }
//        }
//    }

    // gloss

//    logger.info("Inserting Translations...")
//    // XReference forces a dependency on Kanji & Reading
//    data class TranslationWithId(val sense: Translation, val id: Long)
//    val translationIds = hashMapOf<Entry, ArrayList<TranslationWithId>>()
//    transaction {
//        entries.forEach { entry ->
//            // Kanji and Reading tags force a dependency of Sense on Kanji & Reading
//            val ids = arrayListOf<TranslationWithId>()
//            entry.translations?.forEach { translation ->
//                translationQueries.insertTranslation()
//                val translationId = translationQueries.rowid().executeAsOne()
//                ids.add(TranslationWithId(translation, translationId))
//            }
//            translationIds[entry] = ids
//        }
//    }
//
//    transaction {
//        translationIds.keys.forEach { entry ->
//            translationIds[entry]?.forEach { (_, translationId) ->
//                entryQueries.insertEntryTranslationTag(entry.id, translationId)
//            }
//        }
//    }
//
//    transaction {
//        translationIds.keys.forEach { entry ->
//            translationIds[entry]?.forEach { (translation, translationId) ->
//                translation.nameTypes?.forEach { nameType ->
//                    nameTypeQueries.insertNameType(nameType.value, nameType.get())
//                    val nameTypeId = nameTypeQueries.rowid(nameType.value).executeAsOne()
//                    translationQueries.insertTranslationNameTypeTag(translationId, nameTypeId)
//                }
//            }
//        }
//    }
//
//    transaction {
//        translationIds.keys.forEach { entry ->
//            translationIds[entry]?.forEach { (translation, translationId) ->
//                translation.crossReferences?.forEach { xref ->
//                    xReferenceQueries.insertXReference(xref.value)
//                    val xrefId = xReferenceQueries.rowid().executeAsOne()
//                    translationQueries.insertTranslationXRefTag(translationId, xrefId)
//                }
//            }
//        }
//    }
//
//    transaction {
//        translationIds.keys.forEach { entry ->
//            translationIds[entry]?.forEach { (translation, translationId) ->
//                translation.translationDetails?.forEach { detail ->
//                    translationDetailQueries.insertTranslationDetail(detail.value)
//                    val detailId = translationDetailQueries.rowid().executeAsOne()
//                    translationQueries.insertTranslationDetailTag(translationId, detailId)
//                }
//            }
//        }
//    }
}