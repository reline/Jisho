package com.github.reline.jisho.tasks

import com.github.reline.jisho.jmdict.JmdictClient
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toOkioPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class JishoDownloadTask @Inject constructor(
    private val jmdictClient: JmdictClient,
) : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val jmdictVersion: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun download() = runBlocking {
        // todo: inject gradle log level & format; Download <url>, took 83 ms (2.27 kB)
        val destination = outputDir.get().asFile.toOkioPath()
        jmdictClient.downloadDictionaries(destination, jmdictVersion.orNull)
    }
}