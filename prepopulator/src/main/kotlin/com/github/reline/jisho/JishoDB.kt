/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import com.github.reline.jisho.sql.JishoDatabase
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

var buildDir = "prepopulator/build"
var databasePath = "$buildDir/$JISHO_DB"

val url: String get() = "jdbc:sqlite:$databasePath"

val logger: Logger by lazy {
    System.setProperty("java.util.logging.SimpleFormatter.format",
            "%1\$tF %1\$tT - %5\$s%n") // yyyy-MM-dd hh:mm:ss - %s\n
    Logger.getLogger("Jisho")
}

fun Logger.debug(s: String) {
    log(Level.ALL, s)
}

val database: JishoDatabase
    get() {
        logger.info("Loading database driver...")
        // load the JDBC driver first to check if it's working
        Class.forName("org.sqlite.JDBC")
        val driver = JdbcSqliteDriver(url)
        JishoDatabase.Schema.create(driver)
        return JishoDatabase(driver)
    }

fun main() {
    logger.info("Working directory: ${File(".").absolutePath}")

    val db = database
//    KanjiPopulator.run()
//    RadicalPopulator.run()
    DictionaryPopulator(db, OkuriganaPopulator(db)).run()

    logger.info("Done!")
}
