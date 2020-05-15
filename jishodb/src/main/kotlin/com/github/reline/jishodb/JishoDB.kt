package com.github.reline.jishodb

import com.github.reline.jisho.JISHO_DB
import com.github.reline.jisho.sql.JishoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

var databasePath = "jishodb/build/$JISHO_DB"

val url: String by lazy { "jdbc:sqlite:$databasePath" }

val logger: Logger by lazy {
    System.setProperty("java.util.logging.SimpleFormatter.format",
            "%1\$tF %1\$tT - %5\$s%n") // yyyy-MM-dd hh:mm:ss - %s\n
    Logger.getLogger("Jisho")
}

fun Logger.debug(s: String) {
    log(Level.ALL, s)
}

val driver: SqlDriver by lazy {
    logger.info("Loading database driver...")
    // load the JDBC driver first to check if it's working
    Class.forName("org.sqlite.JDBC")
    JdbcSqliteDriver(url)
}

val database: JishoDatabase by lazy {
    JishoDatabase.Schema.create(driver)
    JishoDatabase(driver)
}

fun main() {
    logger.info("Working directory: ${File(".").absolutePath}")
    runKanji()
    runRadicals()
    DictionaryRunner.runDictionaries()
    runOkurigana()
}
