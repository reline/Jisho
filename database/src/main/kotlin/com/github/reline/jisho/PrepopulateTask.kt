package com.github.reline.jisho

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.reline.jisho.populators.DictionaryPopulator
import com.github.reline.jisho.populators.KanjiPopulator
import com.github.reline.jisho.populators.OkuriganaPopulator
import com.github.reline.jisho.sql.JishoDatabase
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class PrepopulateTask : DefaultTask() {
    @get:InputDirectory
    abstract val sourcesDirectory: DirectoryProperty

    @get:OutputFile
    abstract val databaseOutputFile: RegularFileProperty

    override fun getDescription() = "Generate ${databaseOutputFile.get().asFile.name} file"

    @TaskAction
    fun prepopulate() {
        val databaseFile = databaseOutputFile.get().asFile
        databaseFile.ensureParentDirsCreated()
        databaseFile.delete()
        databaseFile.jdbcSqliteDriver.use { driver ->
            val database = JishoDatabase(driver).also { JishoDatabase.Schema.create(driver) }
            val sourcesDir = sourcesDirectory.asFile.get()

            val dictionaries = DictionaryPopulator(database).populate(
                arrayOf(
                    File(sourcesDir, "JMdict_e.xml"),
                    File(sourcesDir, "JMnedict.xml"),
                )
            )

            OkuriganaPopulator(database).populate(
                dictionaries,
                arrayOf(
                    File(sourcesDir, "JmdictFurigana.json"),
                    File(sourcesDir, "JmnedictFurigana.json"),
                )
            )

            // fixme: kanjidic2.xml does not exist
            //  possibly an issue with the sourcesDir, but in that case the above xml files wouldn't be found
            //  might be an issue with the functional test.
            KanjiPopulator(database).populate(
                dictionaries,
                kanji = arrayOf(
                    File(sourcesDir, "kanjidic2.xml"),
                ),
                radk = arrayOf(
                    File(sourcesDir, "radkfile"),
                    File(sourcesDir, "radkfile2"),
                    File(sourcesDir, "radkfilex"),
                ),
                krad = arrayOf(
                    File(sourcesDir, "kradfile"),
                    File(sourcesDir, "kradfile2"),
                ),
            )
        }

        logger.info("Generated ${project.rootDir.toURI().relativize(databaseFile.toURI())}")
    }
}

// todo: https://github.com/xerial/sqlite-jdbc#hint-for-maven-shade-plugin
fun registerJdbcSqliteDriver() {
    Class.forName("org.sqlite.JDBC")
}

val File.jdbcSqliteDriver: SqlDriver get() {
    registerJdbcSqliteDriver()
    return JdbcSqliteDriver("jdbc:sqlite:${this.absolutePath}")
}

fun File.ensureParentDirsCreated() {
    val parentFile = parentFile
    if (!parentFile.exists()) {
        check(parentFile.mkdirs()) {
            "Cannot create parent directories for $this"
        }
    }
}