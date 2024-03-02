package com.github.reline.jisho

import com.github.reline.jisho.jmdict.defaultJmdictClient
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.provideDelegate

abstract class JishoDownloadTask : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val githubToken: Property<String>

    @get:Optional
    @get:Input
    abstract val jmdictVersion: Property<VersionConstraint>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    override fun getDescription() = "Download furigana dictionary files"

    private val jmdictClient by lazy {
        defaultJmdictClient(githubToken.orNull)
    }

    @TaskAction
    fun download() {
        runBlocking {
            jmdictClient.downloadAssets(jmdictVersion.orNull?.requiredVersion, outputDirectory.get().asFile)
        }
    }
}