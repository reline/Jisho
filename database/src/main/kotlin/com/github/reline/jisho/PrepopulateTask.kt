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
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import java.io.File

abstract class PrepopulateTask : DefaultTask() {
    @get:InputDirectory
    abstract val sourcesDirectory: DirectoryProperty

    @get:OutputFile
    abstract val databaseOutputFile: RegularFileProperty

    override fun getDescription() = "Generate ${databaseOutputFile.get().asFile.name} file"

    // todo: https://github.com/xerial/sqlite-jdbc#hint-for-maven-shade-plugin
    private fun registerJdbcSqliteDriver() {
        Class.forName("org.sqlite.JDBC")
    }

    private val File.jdbcSqliteDriver: SqlDriver get() {
        registerJdbcSqliteDriver()
        return JdbcSqliteDriver("jdbc:sqlite:${this.absolutePath}")
    }

    private fun provideDatabase(driver: SqlDriver): JishoDatabase {
        JishoDatabase.Schema.create(driver)
        return JishoDatabase(driver)
    }

    @TaskAction
    fun prepopulate() {
        val databaseFile = databaseOutputFile.get().asFile
        databaseFile.ensureParentDirsCreated()
        databaseFile.delete()
        databaseFile.jdbcSqliteDriver.use {
            val database = provideDatabase(it)
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

            KanjiPopulator(database).populate(
                dictionaries,
                arrayOf(
                    File(sourcesDir, "kanjidic2.xml"),
                ),
                arrayOf(
                    File(sourcesDir, "radkfile"),
                    File(sourcesDir, "radkfile2"),
                    File(sourcesDir, "radkfilex"),
                ),
                arrayOf(
                    File(sourcesDir, "kradfile"),
                    File(sourcesDir, "kradfile2"),
                ),
            )
        }

        logger.info("Generated ${project.rootDir.toURI().relativize(databaseFile.toURI())}")
    }
}