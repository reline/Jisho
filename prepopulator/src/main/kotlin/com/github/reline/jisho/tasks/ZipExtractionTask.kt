package com.github.reline.jisho.tasks

import com.github.reline.jisho.compression.extractZip
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

@CacheableTask
abstract class ZipExtractionTask @Inject constructor() : DefaultTask() {

    @get:PathSensitive(PathSensitivity.NONE)
    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun extract() {
        val compressedFile = inputFile.get().asFile.toOkioPath()
        val destination = FileSystem.SYSTEM to outputDirectory.get().asFile.toOkioPath()
        FileSystem.SYSTEM.extractZip(compressedFile, destination, Charsets.UTF_8)
    }
}
