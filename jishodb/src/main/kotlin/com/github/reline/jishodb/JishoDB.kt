package com.github.reline.jishodb

import com.github.reline.jishodb.dictmodels.Dictionary
import com.github.reline.jishodb.room.RoomSchema
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tickaroo.tikxml.TikXml
import okio.Buffer
import java.io.File
import java.lang.Exception
import java.lang.RuntimeException
import java.sql.*

private const val generateDatabase = true
private const val runDictionaries = true
private const val runRadicals = false
const val debug = false

fun main() {
    println("Loading database driver...")
    // load the JDBC driver first to check if it's working
    Class.forName("org.sqlite.JDBC")

    println("Working directory: ${File(".").absolutePath}")

    if (generateDatabase) {
        val schema = File("schemas").walk().asSequence().first { it.isFile }
        println("""Generating database from schema [${schema.name}]...""")
        JishoDB.createDictionaryTables(schema)
    }

    if (runDictionaries) {
        println("Extracting dictionaries...")
        JishoDB.extractDictionaries(arrayOf(
                File("jishodb/build/dict/JMdict_e.xml"),
                File("jishodb/build/dict/JMnedict.xml")
        ))
    }

    if (runRadicals) {
        println("Extracting radicals...")
        JishoDB.extractRadKFiles(arrayOf(
                File("jishodb/build/dict/radkfile"),
                File("jishodb/build/dict/radkfile2"),
                File("jishodb/build/dict/radkfilex")
        ))

        // TODO: check if radical files cover all kanji already; spoiler alert, I don't think they do
        //            extractKRadFiles(arrayOf(
//                    File("jishodb/build/dict/kradfile"),
//                    File("jishodb/build/dict/kradfile2")
//            ))
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
                println("Inserting ${files[i].name} to database...")
                val start = System.currentTimeMillis()
                insertDictionary(dictionary)
                val end = System.currentTimeMillis()
                println("${files[i].name}: Inserting ${dictionary.entries.size} entries took ${(end - start)}ms")
            }
        }

        fun insertDictionary(dictionary: Dictionary) {
            DriverManager.getConnection("jdbc:sqlite:jishodb/build/jisho.sqlite").use { connection ->
                val statement = connection.createStatement()
                dictionary.entries.forEach {  entry ->
                    connection.prepareStatement("INSERT OR IGNORE INTO Entry(id) VALUES(?)").apply {
                        setInt(1, entry.id)
                    }.performUpdate()
                    entry.kanji?.forEach { kanji ->
                        connection.prepareStatement("INSERT OR IGNORE INTO Kanji(value) VALUES(?)").apply {
                            setString(1, kanji.value)
                        }.performUpdate()
                        connection.prepareStatement("INSERT OR IGNORE INTO EntryKanjiList(entry_id, kanji_id) VALUES(?, ?)").apply {
                            setInt(1, entry.id)
                            setString(2, kanji.value)
                        }.performUpdate()
                        kanji.information?.forEach { info ->
                            connection.prepareStatement("INSERT OR IGNORE INTO KanjiInfo(value) VALUES(?)").apply {
                                setString(1, info.value)
                            }.performUpdate()
                            connection.prepareStatement("INSERT OR IGNORE INTO KanjiInfoList(kanji_id, kanji_info_id) VALUES(?, ?)").apply {
                                setString(1, kanji.value)
                                setString(2, info.value)
                            }.performUpdate()
                        }
                        kanji.priorities?.forEach { priority ->
                            connection.prepareStatement("INSERT OR IGNORE INTO KanjiPriority(value) VALUES(?)").apply {
                                setString(1, priority.value)
                            }.performUpdate()
                            connection.prepareStatement("INSERT OR IGNORE INTO KanjiPriorityList(kanji_id, kanji_priority_id) VALUES(?, ?)").apply {
                                setString(1, kanji.value)
                                setString(2, priority.value)
                            }.performUpdate()
                        }
                    }
                    entry.readings.forEach { reading ->
                        connection.prepareStatement("INSERT OR IGNORE INTO Reading(value, is_not_true_reading) VALUES(?, ?)").apply {
                            setString(1, reading.value)
                            setBoolean(2, reading.isNotTrueReading()) // todo: double check if this can default to false instead of null
                        }.performUpdate()
                        connection.prepareStatement("INSERT OR IGNORE INTO EntryReadingList(entry_id, reading_id) VALUES(?, ?)").apply {
                            setInt(1, entry.id)
                            setString(2, reading.value)
                        }.performUpdate()
                        reading.restrictions?.forEach { restriction ->
                            connection.prepareStatement("INSERT OR IGNORE INTO ReadingRestriction(kanji) VALUES(?)").apply {
                                setString(1, restriction.kanji)
                            }.performUpdate()
                            connection.prepareStatement("INSERT OR IGNORE INTO ReadingRestrictionList(reading_id, restriction_id) VALUES(?, ?)").apply {
                                setString(1, reading.value)
                                setString(2, restriction.kanji)
                            }.performUpdate()
                        }
                        reading.information?.forEach { info ->
                            connection.prepareStatement("INSERT OR IGNORE INTO ReadingInfo(value) VALUES(?)").apply {
                                setString(1, info.value)
                            }.performUpdate()
                            connection.prepareStatement("INSERT OR IGNORE INTO ReadingInfoList(reading_id, reading_info_id) VALUES(?, ?)").apply {
                                setString(1, reading.value)
                                setString(2, info.value)
                            }.performUpdate()
                        }
                        reading.priorities?.forEach { priority ->
                            connection.prepareStatement("INSERT OR IGNORE INTO ReadingPriority(value) VALUES(?)").apply {
                                setString(1, priority.value)
                            }.performUpdate()
                            connection.prepareStatement("INSERT OR IGNORE INTO ReadingPriorityList(reading_id, reading_priority_id) VALUES(?, ?)").apply {
                                setString(1, reading.value)
                                setString(2, priority.value)
                            }.performUpdate()
                        }
                    }
                    entry.senses.forEach { sense ->
                        connection.prepareStatement("INSERT INTO Sense DEFAULT VALUES", Statement.RETURN_GENERATED_KEYS).performUpdate()
                        statement.generatedKeys.use { rs ->
                            val id = rs.getInt("last_insert_rowid()")
                            connection.prepareStatement("INSERT OR IGNORE INTO EntrySenseList(entry_id, sense_id) VALUES(?, ?)").apply {
                                setInt(1, entry.id)
                                setInt(2, id)
                            }.performUpdate()
                            // todo
//                            sense.kanjiTags?.forEach {
//
//                            }
//                            sense.readingTags?.forEach {
//
//                            }
//                            sense.partsOfSpeech?.forEach {
//
//                            }
//                            sense.seeAlso?.forEach {
//
//                            }
//                            sense.antonyms?.forEach {
//
//                            }
//                            sense.fields?.forEach {
//
//                            }
//                            sense.miscellaneous?.forEach {
//
//                            }
//                            sense.information?.forEach {
//
//                            }
//                            sense.sources?.forEach {
//
//                            }
//                            sense.dialects?.forEach {
//
//                            }
                            sense.glosses?.forEach { gloss ->
                                connection.prepareStatement("INSERT OR IGNORE INTO Gloss(value, language, gender) VALUES(?, ?, ?)").apply {
                                    setString(1, gloss.value)
                                    setString(2, gloss.language)
                                    setString(3, gloss.gender)
                                }.performUpdate()
                                connection.prepareStatement("INSERT OR IGNORE INTO SenseGlossList(sense_id, gloss_id) VALUES(?, ?)").apply {
                                    setInt(1, id)
                                    setString(2, gloss.value)
                                }.performUpdate()
                            }
                        }
                    }
                    entry.translations
                }
            }
        }

        fun extractKRadFiles(files: Array<File>) {
            val kradparser = KRadParser()
            files.forEach {
                val krad = kradparser.parse(it)
                // TODO: insert kanji/radicals into db

            }
        }

        fun extractRadKFiles(files: Array<File>) {
            val radkparser = RadKParser()
            files.forEach {
                val radicals = radkparser.parse(it)
                insertRadicals(radicals)
            }
        }

        fun insertRadicals(radicals: List<Radical>) {
            DriverManager.getConnection("jdbc:sqlite:jishodb/build/jisho.sqlite").use { connection ->
                val statement = connection.createStatement()
                // TODO: create tables using room
                statement.perform("CREATE TABLE IF NOT EXISTS Radical(value TEXT NOT NULL PRIMARY KEY, strokes INTEGER NOT NULL)")
                statement.perform("CREATE TABLE IF NOT EXISTS Kanji(value TEXT NOT NULL PRIMARY KEY)")
                statement.perform("CREATE TABLE IF NOT EXISTS KanjiRadical(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, kanji TEXT NOT NULL, radical TEXT NOT NULL, " +
                        "FOREIGN KEY(kanji) REFERENCES Kanji(value), FOREIGN KEY(radical) REFERENCES Radical(value))")

                radicals.forEach { radical ->
                    connection.prepareStatement("INSERT OR IGNORE INTO Radical(value, strokes) VALUES(?, ?)").apply {
                        setString(1, radical.value.toString())
                        setInt(2, radical.strokes)
                    }.performUpdate()
                    radical.kanji.forEach { kanji ->
                        connection.prepareStatement("INSERT OR IGNORE INTO Kanji(value) VALUES(?)").apply {
                            setString(1, kanji.toString())
                        }.performUpdate()
                        connection.prepareStatement("INSERT INTO KanjiRadical(kanji, radical) VALUES(?, ?)").apply {
                            setString(1, kanji.toString())
                            setString(2, radical.value.toString())
                        }.performUpdate()
                    }
                }
            }
        }

        fun createDictionaryTables(file: File) {
            val inputStream = file.inputStream()
            val source = Buffer().readFrom(inputStream)

            val parseStart = System.currentTimeMillis()

            println("Parsing ${file.name}...")

            val schema = try {
                Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                        .adapter(RoomSchema::class.java)
                        .fromJson(source)
            } finally {
                source.clear()
                inputStream.close()
            }

            val parseEnd = System.currentTimeMillis()

            println("${file.name}: Parsing took ${(parseEnd - parseStart)}ms")

            if (schema == null) {
                throw NullPointerException("Resulting database schema was null")
            }

            println("Creating database...")
            val transactionStart = System.currentTimeMillis()

            DriverManager.getConnection("jdbc:sqlite:jishodb/build/jisho.sqlite").use { connection ->
                val statement = connection.createStatement()
                statement.perform(schema.database.createSql()) // schema is pre-sanitized
            }

            val transactionEnd = System.currentTimeMillis()

            println("Database: Creation took ${(transactionEnd - transactionStart)}ms")
        }
    }

}

private fun Statement.perform(query: String) {
    if (debug) {
        println(query)
    }
    try {
        executeUpdate(query)
    } catch (e: Exception) {
        throw RuntimeException(query, e)
    }
}

private fun PreparedStatement.performUpdate() {
    if (debug) {
        println(this)
    }
    try {
        executeUpdate()
    } catch (e: Exception) {
        throw RuntimeException(this.toString(), e)
    }
}

private fun Boolean.toInt(): Int {
    return if (this) {
        1
    } else {
        0
    }
}
