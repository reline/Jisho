package com.github.reline.jisho.tasks

import com.github.reline.jisho.jdbcSqliteDriver
import com.github.reline.jisho.populators.DictionaryPopulator
import com.github.reline.jisho.populators.KanjiPopulator
import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.touch
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class JishoPopulateTask : DefaultTask() {
    @get:InputFile
    abstract val jmdict: RegularFileProperty

    @get:InputFile
    abstract val jmdictFurigana: RegularFileProperty

    @get:InputFile
    abstract val jmnedict: RegularFileProperty

    @get:InputFile
    abstract val jmnedictFurigana: RegularFileProperty

    @get:InputFiles
    abstract val kanji: ConfigurableFileCollection

    @get:InputFiles
    abstract val radicalKanjiMappings: ConfigurableFileCollection

    @get:InputFiles
    abstract val kanjiRadicalMappings: ConfigurableFileCollection

    @get:OutputFile
    abstract val databaseOutputFile: RegularFileProperty

    private val fileSystem = FileSystem.SYSTEM

    @TaskAction
    fun prepopulate() {
        val databasePath = databaseOutputFile.get().asFile.toOkioPath()
        fileSystem.touch(databasePath)
        databasePath.jdbcSqliteDriver.use { driver ->
            val database = JishoDatabase(driver).also { JishoDatabase.Schema.create(driver) }

            val dictpop = DictionaryPopulator(database)
            val dictionaries = listOf(
                dictpop.populate(dictionaryFile = jmdict.get().asFile, okuriganaFile = jmdictFurigana.get().asFile),
                dictpop.populate(dictionaryFile = jmnedict.get().asFile, okuriganaFile = jmnedictFurigana.get().asFile),
            )

            KanjiPopulator(database).populate(
                dictionaries,
                kanji = kanji.files,
                radk = radicalKanjiMappings.files,
                krad = kanjiRadicalMappings.files,
            )
        }

        logger.info("Generated ${project.rootDir.relativeTo(databasePath.toFile()).path}")
    }
}
