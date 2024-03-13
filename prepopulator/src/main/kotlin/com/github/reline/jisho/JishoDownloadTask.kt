package com.github.reline.jisho

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

    // todo: research task invalidation, would an indeterminate number of output files work better?
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    override fun getDescription() = "Download furigana dictionary files"
    override fun getGroup() = JishoDatabasePopulatorPlugin.GROUP

    @TaskAction
    fun download() = runBlocking {
        // todo: inject gradle log level & format; Download <url>, took 83 ms (2.27 kB)
        jmdictClient.downloadDictionaries(outputDir.get().asFile.toOkioPath(), jmdictVersion.orNull)
    }
}