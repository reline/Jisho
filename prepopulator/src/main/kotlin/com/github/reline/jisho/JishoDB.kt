/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import com.github.reline.jisho.sql.JishoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.sqlite.SQLiteConfig
import java.io.File
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

var buildDir = "prepopulator/build"
var databasePath = "$buildDir/$JISHO_DB"

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

    val properties = Properties().also {
        // https://www.sqlite.org/compile.html#default_foreign_keys
        it[SQLiteConfig.Pragma.FOREIGN_KEYS.pragmaName] = "true"
    }
    JdbcSqliteDriver(url, properties)
}

val database: JishoDatabase by lazy {
    JishoDatabase.Schema.create(driver)
    JishoDatabase(driver)
}

fun main() {
    logger.info("Working directory: ${File(".").absolutePath}")
    KanjiPopulator.run()
    RadicalPopulator.run()
    DictionaryPopulator.run()
    OkuriganaPopulator.run()
}
