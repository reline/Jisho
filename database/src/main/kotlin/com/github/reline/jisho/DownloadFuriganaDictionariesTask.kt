package com.github.reline.jisho

import com.github.reline.jisho.jmdict.defaultJmdictClient
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.provideDelegate

abstract class DownloadFuriganaDictionariesTask : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val bearerToken: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    override fun getDescription() = "Download furigana dictionary files"

    private val jmdictClient by lazy {
        defaultJmdictClient(bearerToken.orNull)
    }

    @TaskAction
    fun download() {
        jmdictClient.downloadLatestAssets(outputDirectory.get().asFile)
    }
}