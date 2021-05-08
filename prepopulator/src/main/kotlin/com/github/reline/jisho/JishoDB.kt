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

const val buildDir = "prepopulator/build"
private const val databasePath = "$buildDir/$JISHO_DB"

private val database: JishoDatabase by lazy {
    provideDatabase("jdbc:sqlite:$databasePath")
}

fun provideDatabase(url: String): JishoDatabase {
    logger.info("Loading database driver...")
    // load the JDBC driver first to check if it's working
    Class.forName("org.sqlite.JDBC")
    val driver = JdbcSqliteDriver(url)
    JishoDatabase.Schema.create(driver)
    return JishoDatabase(driver)
}

fun main() {
    logger.info("Working directory: ${File(".").absolutePath}")

    val dictionaries = DictionaryPopulator(database).populate(
        arrayOf(
            File("$buildDir/dict/JMdict_e.xml"),
            File("$buildDir/dict/JMnedict.xml"),
        )
    )

    OkuriganaPopulator(database).populate(
        dictionaries,
        arrayOf(
            File("$buildDir/dict/JmdictFurigana.json"),
            File("$buildDir/dict/JmnedictFurigana.json"),
        )
    )

    KanjiPopulator(database).populate(
        dictionaries,
        arrayOf(
            File("$buildDir/dict/kanjidic2.xml"),
        ),
        arrayOf(
            File("$buildDir/dict/radkfile"),
            File("$buildDir/dict/radkfile2"),
            File("$buildDir/dict/radkfilex"),
        ),
        arrayOf(
            File("$buildDir/dict/kradfile"),
            File("$buildDir/dict/kradfile2"),
        ),
    )

    logger.info("Done!")
}
