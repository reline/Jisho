package com.github.reline.jishodb

import com.github.reline.jishodb.JishoDB.Companion.debug
import com.github.reline.jishodb.dictmodels.Dictionary
import com.tickaroo.tikxml.TikXml
import okio.Buffer
import java.io.File
import java.lang.Exception
import java.lang.RuntimeException
import java.sql.DriverManager
import java.sql.Statement

class JishoDB {

    companion object {

        private const val runDictionaries = false
        private const val runRadicals = true
        const val debug = false

        @JvmStatic
        fun main(args: Array<String>) {
            println("Loading database driver...")
            // load the JDBC driver first to check if it's working
            Class.forName("org.sqlite.JDBC")

            if (runDictionaries) {
                println("Extracting dictionaries...")
                extractDictionaries(arrayOf(
                        File("jishodb/build/dict/JMdict_e.xml"),
                        File("jishodb/build/dict/JMnedict.xml")
                ))
            }

            if (runRadicals) {
                println("Extracting radicals...")
                extractRadKFiles(arrayOf(
                        File("jishodb/build/dict/radkfile"),
                        File("jishodb/build/dict/radkfile2"),
                        File("jishodb/build/dict/radkfilex")
                ))

                // TODO: check if radical files cover all kanji already
                //            extractKRadFiles(arrayOf(
//                    File("jishodb/build/dict/kradfile"),
//                    File("jishodb/build/dict/kradfile2")
//            ))
            }
        }

        private fun extractDictionaries(files: Array<File>) {
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

                println("${file.name}: Parsing ${dictionary.entries.size} entries took ${(parseEnd - parseStart) / 1000} seconds")

                dictionaries.add(dictionary)
            }

            dictionaries.forEach { dictionary ->
                insertDictionary(dictionary)
            }
        }

        private fun insertDictionary(dictionary: Dictionary) {
            DriverManager.getConnection("jdbc:sqlite:jishodb/build/jisho.sqlite").use { connection ->
                val statement = connection.createStatement()
                //
//                statement.executeUpdate(dictionary.statement)

//            statement.executeUpdate("drop table if exists person")
//            statement.executeUpdate("create table person (id integer, name string)")
//            statement.executeUpdate("insert into person values(1, 'leo')")
//            statement.executeUpdate("insert into person values(2, 'yui')")
//            val rs = statement.executeQuery("select * from person")
//            while (rs.next()) {
//                 read the result set
//                println("name = ${rs.getString("name")}")
//                println("id = ${rs.getInt("id")}")
//            }
            }
        }

        private fun extractKRadFiles(files: Array<File>) {
            val kradparser = KRadParser()
            files.forEach {
                val krad = kradparser.parse(it)
                // TODO: insert kanji/radicals into db

            }
        }

        private fun extractRadKFiles(files: Array<File>) {
            val radkparser = RadKParser()
            files.forEach {
                val radk = radkparser.parse(it)
                insertRadicals(radk)
            }
        }

        private fun insertRadicals(radicals: List<Radical>) {
            DriverManager.getConnection("jdbc:sqlite:jishodb/build/jisho.sqlite").use { connection ->
                val statement = connection.createStatement()
                // TODO: create tables using room
                statement.perform("CREATE TABLE IF NOT EXISTS Radical(value TEXT NOT NULL PRIMARY KEY, strokes INTEGER NOT NULL)")
                statement.perform("CREATE TABLE IF NOT EXISTS Kanji(value TEXT NOT NULL PRIMARY KEY)")
                statement.perform("CREATE TABLE IF NOT EXISTS KanjiRadical(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, kanji TEXT NOT NULL, radical TEXT NOT NULL, " +
                        "FOREIGN KEY(kanji) REFERENCES Kanji(value), FOREIGN KEY(radical) REFERENCES Radical(value))")

                radicals.forEach { radical ->
                    statement.perform("""INSERT OR IGNORE INTO Radical VALUES ("${radical.value}", ${radical.strokes})""")
                    radical.kanji.forEach { kanji ->
                        statement.perform("""INSERT OR IGNORE INTO Kanji VALUES ("$kanji")""")
                        statement.perform("""INSERT INTO KanjiRadical(kanji, radical) VALUES ("$kanji", "${radical.value}")""")
                    }
                }
            }
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

private fun Boolean.toInt(): Int {
    return if (this) {
        1
    } else {
        0
    }
}
