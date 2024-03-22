package com.github.reline.jisho.tasks

import com.github.reline.jisho.jmdict.JmdictClient
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toOkioPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class JishoDownloadTask @Inject constructor(
    private val jmdictClient: JmdictClient,
) : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val jmdictVersion: Property<String>

    @get:Input
    abstract val outputDir: DirectoryProperty

    // fixme: Could not determine the dependencies of task ':populateJishoDatabase'.
    //> Could not create task ':downloadJishoSources'.
    //   > Could not create task of type 'JishoDownloadTask'.
    //      > Could not generate a decorated class for type JishoDownloadTask.
    //         > Cannot have abstract method JishoDownloadTask.getOutputFiles().
    @get:OutputFiles
    abstract val outputFiles: FileCollection

    @TaskAction
    fun download() = runBlocking {
        // todo: inject gradle log level & format; Download <url>, took 83 ms (2.27 kB)
        val destination = outputDir.get().asFile.toOkioPath()
        val files = jmdictClient.downloadDictionaries(destination, jmdictVersion.orNull)
        outputFiles.files.addAll(files.map { it.toFile() })
    }
}