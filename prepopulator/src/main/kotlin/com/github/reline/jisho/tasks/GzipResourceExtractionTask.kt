package com.github.reline.jisho.tasks

import com.github.reline.jisho.compression.extractGzip
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

@CacheableTask
abstract class GzipResourceExtractionTask @Inject constructor() : DefaultTask() {

    @get:Input
    abstract val resourceAssetPath: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun extract() {
        val compressedFile = resourceAssetPath.get().toPath()
        val destination = FileSystem.SYSTEM to outputFile.get().asFile.toOkioPath()
        FileSystem.RESOURCES.extractGzip(compressedFile, destination)
    }
}
