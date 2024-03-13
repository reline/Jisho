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

abstract class JishoPopulateTask : DefaultTask() {
    @get:InputDirectory
    abstract val dictionarySourcesDirectory: DirectoryProperty

    @get:InputDirectory
    abstract val furiganaSourcesDirectory: DirectoryProperty

    @get:OutputFile
    abstract val databaseOutputFile: RegularFileProperty

    override fun getDescription() = "Generate ${databaseOutputFile.get().asFile.name} file"
    override fun getGroup() = JishoDatabasePopulatorPlugin.GROUP

    @TaskAction
    fun prepopulate() {
        val databaseFile = databaseOutputFile.get().asFile
        databaseFile.parentFile.ensureDirsCreated()
        databaseFile.delete()
        databaseFile.jdbcSqliteDriver.use { driver ->
            val database = JishoDatabase(driver).also { JishoDatabase.Schema.create(driver) }
            val dictSourcesDir = dictionarySourcesDirectory.asFile.get()

            val dictionaries = DictionaryPopulator(database).populate(
                arrayOf(
                    File(dictSourcesDir, "JMdict_e.xml"),
                    File(dictSourcesDir, "JMnedict.xml"),
                )
            )

            val furiganaSources = furiganaSourcesDirectory.get().asFile
            OkuriganaPopulator(database).populate(
                dictionaries,
                arrayOf(
                    File(furiganaSources, "JmdictFurigana.json"),
                    File(furiganaSources, "JmnedictFurigana.json"),
                )
            )

            // fixme: kanjidic2.xml does not exist
            //  possibly an issue with the sourcesDir, but in that case the above xml files wouldn't be found
            //  might be an issue with the functional test.
            KanjiPopulator(database).populate(
                dictionaries,
                kanji = arrayOf(
                    File(dictSourcesDir, "kanjidic2.xml"),
                ),
                radk = arrayOf(
                    File(dictSourcesDir, "radkfile"),
                    File(dictSourcesDir, "radkfile2"),
                    File(dictSourcesDir, "radkfilex"),
                ),
                krad = arrayOf(
                    File(dictSourcesDir, "kradfile"),
                    File(dictSourcesDir, "kradfile2"),
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
