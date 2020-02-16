package com.github.reline.jishodb

import com.github.reline.jisho.JishoDatabase
import com.github.reline.jishodb.JishoDB.Companion.extractKRadFiles
import com.github.reline.jishodb.dictmodels.Radical
import com.github.reline.jishodb.dictmodels.jmdict.Dictionary
import com.github.reline.jishodb.dictmodels.kanji.KanjiDictionary
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.tickaroo.tikxml.TikXml
import okio.Buffer
import java.io.File

private const val runDictionaries = false
private const val runRadicals = true
private const val runKanji = true

private lateinit var database: JishoDatabase

fun main() {
    println("Loading database driver...")
    // load the JDBC driver first to check if it's working
    Class.forName("org.sqlite.JDBC")

    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:jishodb/build/jisho.sqlite")
    JishoDatabase.Schema.create(driver)
    database = JishoDatabase(driver)

    println("Working directory: ${File(".").absolutePath}")

    if (runKanji) {
        println("Extracting kanji...")
        JishoDB.extractKanji(arrayOf(
                File("jishodb/build/dict/kanjidic2.xml")
        ))
    }

    if (runRadicals) {
        println("Extracting radicals...")
        JishoDB.extractRadKFiles(arrayOf(
                File("jishodb/build/dict/radkfile"),
                File("jishodb/build/dict/radkfile2"),
                File("jishodb/build/dict/radkfilex")
        ))

        extractKRadFiles(arrayOf(
                File("jishodb/build/dict/kradfile"),
                File("jishodb/build/dict/kradfile2")
        ))
    }

    if (runDictionaries) {
        println("Extracting dictionaries...")
        JishoDB.extractDictionaries(arrayOf(
                File("jishodb/build/dict/JMdict_e.xml"),
                File("jishodb/build/dict/JMnedict.xml")
        ))
    }
}


class JishoDB {

    companion object {

        fun extractDictionaries(files: Array<File>) {
            val dictionaries = ArrayList<Dictionary>()
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

                println("${file.name}: Parsing ${dictionary.entries.size} entries took ${(parseEnd - parseStart)}ms")

                dictionaries.add(dictionary)
            }

            dictionaries.forEachIndexed { i, dictionary ->
                val start = System.currentTimeMillis()
                println("Inserting ${files[i].name} to database...")
                insertDictionary(dictionary)
                val end = System.currentTimeMillis()
                println("${files[i].name}: Inserting ${dictionary.entries.size} entries took ${(end - start)}ms")
            }
        }

        fun insertDictionary(dictionary: Dictionary) = with(database) {
            println("Inserting Entries & Kanji...")
            transaction {
                dictionary.entries.forEach { entry ->
                    entryQueries.insertEntry(entry.id, entry.isCommon())

                    entry.kanji?.forEach { kanji ->
                        kanjiElementQueries.insertKanji(kanji.value, kanji.isCommon())
                        val kanjiId = kanjiElementQueries.rowid().executeAsOne()
                        entryQueries.insertEntryKanjiTag(entry.id, kanjiId)

                        kanji.information?.forEach { info ->
                            infoQueries.insertInfo(info.value, info.getInfo())
                            val infoId = infoQueries.rowid(info.value).executeAsOne()
                            kanjiElementQueries.insertKanjiInfoTag(kanjiId, infoId)
                        }

                        kanji.priorities?.forEach { priority ->
                            priorityQueries.insertPriority(priority.value, priority.isCommon())
                            val priorityId = priorityQueries.rowid(priority.value).executeAsOne()
                            kanjiElementQueries.insertKanjiPriorityTag(kanjiId, priorityId)
                        }
                    }
                }
            }

            // Reading Restriction forces a dependency of Reading on Kanji

            println("Inserting Readings...")
            transaction {
                dictionary.entries.forEach { entry ->
                    entry.readings.forEach { reading ->
                        readingQueries.insertReading(reading.value, reading.isNotTrueReading(), reading.isCommon())
                        val readingId = readingQueries.rowid().executeAsOne()
                        entryQueries.insertEntryReadingTag(entry.id, readingId)

                        reading.restrictions?.forEach { restriction ->
                            readingQueries.insertReadingRestrictionTag(readingId, restriction.kanji)
                        }
                        reading.information?.forEach { info ->
                            infoQueries.insertInfo(info.value, info.info)
                            val infoId = infoQueries.rowid(info.value).executeAsOne()
                            readingQueries.insertReadingInfoTag(readingId, infoId)
                        }
                        reading.priorities?.forEach { priority ->
                            priorityQueries.insertPriority(priority.value, priority.isCommon())
                            val priorityId = priorityQueries.rowid(priority.value).executeAsOne()
                            readingQueries.insertReadingPriorityTag(readingId, priorityId)
                        }
                    }
                }
            }

            println("Inserting Senses & Translations...")
            transaction {
                dictionary.entries.forEach { entry ->

                    // Kanji and Reading tags force a dependency of Sense on Kanji & Reading
                    entry.senses.forEach { sense ->
                        senseQueries.insertSense()
                        val senseId = senseQueries.rowid().executeAsOne()
                        entryQueries.insertEntrySenseTag(entry.id, senseId)

                        sense.kanjiTags?.forEach {
                            senseQueries.insertSenseKanjiTag(senseId, it.value)
                        }

                        sense.readingTags?.forEach {
                            senseQueries.insertSenseReadingTag(senseId, it.value)
                        }

                        sense.partsOfSpeech?.forEach {
                            partOfSpeechQueries.insertPartOfSpeech(it.value)
                            val posId = partOfSpeechQueries.rowid(it.value).executeAsOne()
                            senseQueries.insertSensePosTag(senseId, posId)
                        }
                        sense.seeAlso?.forEach {
                            xReferenceQueries.insertXReference(it.value)
                            val xrefId = xReferenceQueries.rowid().executeAsOne()
                            senseQueries.insertSenseXRefTag(senseId, xrefId)
                        }
                        sense.antonyms?.forEach { antonym ->
                            antonymQueries.insertAntonym(senseId, antonym.value)
                        }
                        sense.fields?.forEach {
                            fieldQueries.insertField(it.value)
                            val fieldId = fieldQueries.rowid(it.value).executeAsOne()
                            senseQueries.insertSenseFieldTag(senseId, fieldId)
                        }
                        sense.miscellaneous?.forEach {
                            infoQueries.insertInfo(it.value, it.getTag())
                            val miscId = infoQueries.rowid(it.value).executeAsOne()
                            senseQueries.insertSenseMiscTag(senseId, miscId)
                        }
                        sense.information?.forEach {
                            senseInfoQueries.insertSenseInfo(it.value)
                            val infoId = senseInfoQueries.rowid().executeAsOne()
                            senseQueries.insertSenseInfoTag(senseId, infoId)
                        }
                        sense.sources?.forEach {
                            sourceQueries.insertSource(it.value, it.language, it.isFullDescription(), it.isWaseieigo())
                            val sourceId = sourceQueries.rowid().executeAsOne()
                            senseQueries.insertSenseSourceTag(senseId, sourceId)
                        }
                        sense.dialects?.forEach { dialect ->
                            dialectQueries.insertDialect(dialect.value)
                            val dialectId = dialectQueries.rowid(dialect.value).executeAsOne()
                            senseQueries.insertSenseDialectTag(senseId, dialectId)
                        }
                        sense.glosses?.forEach { gloss ->
                            glossQueries.insertGloss(gloss.value, gloss.language, gloss.gender)
                            val glossId = glossQueries.rowid().executeAsOne()
                            senseQueries.insertSenseGlossTag(senseId, glossId)
                        }
                    }

                    // XReference forces a dependency on Kanji & Reading
                    entry.translations?.forEach { translation ->
                        translationQueries.insertTranslation()
                        val translationId = translationQueries.rowid().executeAsOne()
                        entryQueries.insertEntryTranslationTag(entry.id, translationId)

                        translation.nameTypes?.forEach { nameType ->
                            nameTypeQueries.insertNameType(nameType.value, nameType.get())
                            val nameTypeId = nameTypeQueries.rowid(nameType.value).executeAsOne()
                            translationQueries.insertTranslationNameTypeTag(translationId, nameTypeId)
                        }

                        translation.crossReferences?.forEach { xref ->
                            xReferenceQueries.insertXReference(xref.value)
                            val xrefId = xReferenceQueries.rowid().executeAsOne()
                            translationQueries.insertTranslationXRefTag(translationId, xrefId)
                        }

                        translation.translationDetails?.forEach { detail ->
                            translationDetailQueries.insertTranslationDetail(detail.value)
                            val detailId = translationDetailQueries.rowid().executeAsOne()
                            translationQueries.insertTranslationDetailTag(translationId, detailId)
                        }
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

        fun extractRadKFiles(files: Array<File>) {
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

        fun insertRadk(radicals: List<Radical>) = with(database) {
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

        fun extractKanji(files: Array<File>) {
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
                    kanjiRadicalQueries.insertKanji(it.literal)
                }
            }
        }
    }

}