package com.github.reline.jisho.tasks

import com.github.reline.jisho.jdbcSqliteDriver
import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.touch
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class JishoPopulateTask : DefaultTask() {
    @get:InputFiles
    abstract val dictionarySources: ConfigurableFileCollection

    @get:InputFiles
    abstract val furiganaSources: ConfigurableFileCollection

    @get:InputFiles
    abstract val kanjiSources: ConfigurableFileCollection

    @get:InputFiles
    abstract val radicalSources: ConfigurableFileCollection

    @get:OutputFile
    abstract val databaseOutputFile: RegularFileProperty

    private val fileSystem = FileSystem.SYSTEM

    @TaskAction
    fun prepopulate() {
        val databasePath = databaseOutputFile.get().asFile.toOkioPath()
        fileSystem.touch(databasePath)
        databasePath.jdbcSqliteDriver.use { driver ->
            val database = JishoDatabase(driver).also { JishoDatabase.Schema.create(driver) }
            val dictSources = dictionarySources.files.associateBy { it.name }
            val kanjiFiles = kanjiSources.files.associateBy { it.name }

//            val dictionaries = DictionaryPopulator(database).populate(
//                arrayOf(
//                    dictSources.getValue("JMdict_e.xml"),
//                    dictSources.getValue("JMnedict.xml"),
//                )
//            )
//
//            val furiganaSources = furiganaSources.files.associateBy { it.name }
//            OkuriganaPopulator(database).populate(
//                dictionaries,
//                arrayOf(
//                    furiganaSources.getValue("JmdictFurigana.json"),
//                    furiganaSources.getValue("JmnedictFurigana.json"),
//                )
//            )
//
//            KanjiPopulator(database).populate(
//                dictionaries,
//                kanji = arrayOf(
//                    kanjiFiles.getValue("kanjidic2.xml"),
//                ),
//                radk = arrayOf(
//                    kanjiFiles.getValue("radkfile"),
//                    kanjiFiles.getValue("radkfile2"),
//                    kanjiFiles.getValue("radkfilex"),
//                ),
//                krad = arrayOf(
//                    kanjiFiles.getValue("kradfile"),
//                    kanjiFiles.getValue("kradfile2"),
//                ),
//            )
        }

        logger.info("Generated ${project.rootDir.relativeTo(databasePath.toFile()).path}")
    }
}
