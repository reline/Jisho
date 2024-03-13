package com.github.reline.jisho

import com.github.reline.jisho.compression.DictionaryFileDecompressor
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

// todo: unit test
// todo: break this out into separate extraction tasks to improve caching when only one extraction fails
abstract class JishoExtractionTask : DefaultTask() {
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    override fun getDescription() = "Inflate monash dictionary files"
    override fun getGroup() = JishoDatabasePopulatorPlugin.GROUP

    // todo: inject inflator / file system for testing this task
    private val extractor by lazy { DictionaryFileDecompressor() }

    @TaskAction
    fun extract() {
        // todo: use an input directory property to improve invalidation and caching
        extractor.extractAll(".".toPath(), outputDirectory.get().asFile.toOkioPath())
    }
}