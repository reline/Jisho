package com.github.reline.jisho.tasks

import com.github.reline.jisho.jmdict.JmdictClient
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okio.Path.Companion.toOkioPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

@CacheableTask
abstract class JishoDownloadTask @Inject constructor(
    private val jmdictClient: Provider<JmdictClient>,
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
        val client = jmdictClient.get()
        val version = jmdictVersion.orNull
        withTimeout(timeout.orNull?.toKotlinDuration() ?: Duration.INFINITE) {
            client.downloadDictionaries(destination, version)
        }
    }
}