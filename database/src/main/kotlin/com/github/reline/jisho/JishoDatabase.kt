package com.github.reline.jisho

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import java.io.File
import javax.inject.Inject

abstract class JishoDatabase @Inject constructor(
    private val project: Project,
    objectFactory: ObjectFactory,
) {

    private val intermediateSourcesDirectory
        get() = File(project.buildDir, "intermediates/jisho")

    val destination = objectFactory.fileProperty().convention {
        File(project.buildDir, "generated/jisho/database/jisho.sqlite")
    }

    internal fun registerTasks(githubToken: Property<String>) {
        val downloadTask = project.tasks.register("downloadJishoSources", DownloadFuriganaDictionariesTask::class.java) {
            bearerToken.set(githubToken)
            outputDirectory.set(File(intermediateSourcesDirectory, "furigana"))
            group = JishoPlugin.GROUP
        }
        val prepareTask = project.tasks.register("prepareJishoSources", InflateDictionariesTask::class.java) {
            outputDirectory.set(File(intermediateSourcesDirectory, "dictionaries"))
            group = JishoPlugin.GROUP
        }
        // todo: consolidate into prepare task
        val copyTask = project.tasks.register("copyJishoSources", Copy::class.java) {
            dependsOn(downloadTask)
            dependsOn(prepareTask)
            group = JishoPlugin.GROUP

            from(downloadTask.get().outputDirectory)
            from(prepareTask.get().outputDirectory)
            into(File(intermediateSourcesDirectory, "merged"))
            includeEmptyDirs = false
        }
        val prepopulateTask = project.tasks.register("prepopulateJishoDatabase", PrepopulateTask::class.java) {
            sourcesDirectory.set(copyTask.get().destinationDir)
            databaseOutputFile.set(destination)
            group = JishoPlugin.GROUP
            dependsOn(copyTask)
        }

        project.tasks
            .filter { task -> task.name.startsWith("assemble") }
            .forEach { assembleTask -> assembleTask.dependsOn(prepopulateTask) }
    }
}