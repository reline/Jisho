package com.github.reline.jishodb

import com.github.reline.jishodb.dictmodels.JMdict
import com.tickaroo.tikxml.TikXml
import okio.Buffer
import java.io.File
import java.sql.DriverManager

class JishoDB {

    companion object {

        private const val TABLE = "Word"

        @JvmStatic
        fun main(args: Array<String>) {
            Class.forName("org.sqlite.JDBC")

            val file = File("jishodb/src/main/resources/raw/jmdict_e")
            val inputStream = file.inputStream()
            val source = Buffer().readFrom(inputStream)

            val parseStart = System.currentTimeMillis()

            val jmDict: JMdict = TikXml.Builder()
                    .exceptionOnUnreadXml(false)
                    .build()
                    .read(source, JMdict::class.java)
            source.clear()
            inputStream.close()

            val parseEnd = System.currentTimeMillis()

            println("Parsing ${jmDict.entries.size} entries took ${(parseEnd - parseStart) / 1000} seconds")

        DriverManager.getConnection("jdbc:sqlite:jishodb/out/jisho.sqlite").use { connection ->
            val statement = connection.createStatement()

//            statement.executeUpdate("DROP TABLE IF EXISTS WORD")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS WORD (id INTEGER PRIMARY KEY, isCommon INTEGER, attribution TEXT)")

//            statement.executeUpdate("DROP TABLE IF EXISTS ATTRIBUTION")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ATTRIBUTION (id INTEGER PRIMARY KEY AUTOINCREMENT, isJmdict INTEGER, isJmnedict INTEGER)")

//            statement.executeUpdate("DROP TABLE IF EXISTS TAG")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS TAG (id INTEGER PRIMARY KEY AUTOINCREMENT, word_id INTEGER, tag TEXT)")

//            statement.executeUpdate("DROP TABLE IF EXISTS JAPANESE")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS JAPANESE (id INTEGER PRIMARY KEY AUTOINCREMENT, word_id INTEGER, word TEXT, reading TEXT)")

//            statement.executeUpdate("DROP TABLE IF EXISTS ENGLISH_DEFINITION")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ENGLISH_DEFINITION (id INTEGER PRIMARY KEY AUTOINCREMENT, word_id INTEGER, value TEXT)")

//            statement.executeUpdate("DROP TABLE IF EXISTS PART_OF_SPEECH")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS PART_OF_SPEECH (id INTEGER PRIMARY KEY AUTOINCREMENT, word_id INTEGER, value TEXT)")

//            statement.executeUpdate("DROP TABLE IF EXISTS LINK")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS LINK (id INTEGER PRIMARY KEY AUTOINCREMENT, word_id INTEGER, text TEXT, url TEXT)")

//            statement.executeUpdate("DROP TABLE IF EXISTS SEE_ALSO")
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS SEE_ALSO (id INTEGER PRIMARY KEY AUTOINCREMENT, word_id INTEGER, value TEXT)")

            jmDict.entries.forEach {
                val attributionID = statement.executeUpdate("INSERT INTO ATTRIBUTION values()")
                statement.executeUpdate("INSERT INTO WORD values(${it.id}, ${it.isCommon().toInt()}, $attributionID)")
            }

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

    }

}

private fun Boolean.toInt(): Int {
    return if (this) {
        1
    } else {
        0
    }
}
