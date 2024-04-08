package com.github.reline.jisho.tasks

import com.github.reline.jisho.compression.extractZip
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class ZipResourceExtractionTask @Inject constructor() : DefaultTask() {

    @get:Input
    abstract val resourceAssetPath: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun extract() {
        val compressedFile = resourceAssetPath.get().toPath()
        val destination = FileSystem.SYSTEM to outputDirectory.get().asFile.toOkioPath()
        FileSystem.RESOURCES.extractZip(compressedFile, destination)
    }
}