package com.github.reline.jisho.tasks

import com.github.reline.jisho.populators.DictionaryInput
import com.github.reline.jisho.populators.JishoInput
import com.github.reline.jisho.populators.JishoPopulator
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.listProperty
import java.io.File
import java.io.Serializable
import javax.inject.Inject
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.toKotlinDuration

data class Dictionary(
    @get:InputFile
    override val definitions: File,
    @get:InputFile
    override val okurigana: File,
) : DictionaryInput, Serializable

abstract class JishoPopulateTask @Inject constructor(
    objectFactory: ObjectFactory
) : DefaultTask() {
    @get:Input
    val dictionaries = objectFactory.listProperty<Dictionary>().empty()

    @get:InputFiles
    abstract val kanji: ConfigurableFileCollection

    @get:InputFiles
    abstract val radicalKanjiMappings: ConfigurableFileCollection

    @get:InputFiles
    abstract val kanjiRadicalMappings: ConfigurableFileCollection

    @get:Input
    abstract val databaseFileName: Property<String>

    // build/generated/assets/populateJishoDatabase
    @get:OutputDirectory
    abstract val databaseOutputDirectory: DirectoryProperty

    // todo: inject
    private val jishoPopulator = JishoPopulator()

    /**
     * In order to maintain data integrity, a previously generated database cannot be reused and
     * therefore this task unfortunately cannot be incremental.
     */
    @TaskAction
    fun prepopulate() = runBlocking {
        val database = databaseOutputDirectory.file(databaseFileName.get()).get().asFile
        if (database.exists()) return@runBlocking // fixme: enable caching
        // fixme: timeout cli param
        try {
            withTimeout(timeout.orNull?.toKotlinDuration() ?: INFINITE) {
                jishoPopulator.populate(
                    database,
                    JishoInput(
                        dictionaries = dictionaries.get(),
                        kanji = kanji.files,
                        radicals = radicalKanjiMappings.files,
                    ),
                )
            }
        } catch (e: Exception) {
            database.delete()
            throw e
        }
        logger.info("Generated ${database.path}")
    }
}
