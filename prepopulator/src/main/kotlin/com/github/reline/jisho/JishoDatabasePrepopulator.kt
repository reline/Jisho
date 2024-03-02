package com.github.reline.jisho

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.io.File
import javax.inject.Inject

abstract class JishoDatabasePrepopulator @Inject constructor(
    private val project: Project,
    objectFactory: ObjectFactory,
) {

    private val buildDir: File
        get() = project.layout.buildDirectory.get().asFile

    private val intermediateSourcesDirectory
        get() = File(buildDir, "intermediates/jisho")

    val destination = objectFactory.fileProperty().convention {
        File(buildDir, "generated/jisho/database/jisho.sqlite")
    }

    internal fun registerTasks(githubToken: Property<String>, jmdictVersion: Property<VersionConstraint>) {
        val downloadTask = project.tasks.register("downloadJishoSources", JishoDownloadTask::class.java) {
            this.githubToken.set(githubToken)
            this.jmdictVersion.set(jmdictVersion)
            outputDirectory.set(File(intermediateSourcesDirectory, "furigana"))
            group = JishoDatabasePlugin.GROUP
        }
        val prepareTask = project.tasks.register("prepareJishoSources", JishoExtractionTask::class.java) {
            outputDirectory.set(File(intermediateSourcesDirectory, "dictionaries"))
            group = JishoDatabasePlugin.GROUP
        }
        project.tasks.register("prepopulateJishoDatabase", JishoPrepopulateTask::class.java) {
            sourcesDirectory.set(intermediateSourcesDirectory)
            databaseOutputFile.set(destination)
            group = JishoDatabasePlugin.GROUP
            dependsOn(downloadTask)
            dependsOn(prepareTask)
        }
    }
}