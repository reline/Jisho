package com.github.reline.jisho

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.model.ObjectFactory
import java.io.File
import javax.inject.Inject

abstract class JishoDatabase @Inject constructor(
    private val project: Project,
    objectFactory: ObjectFactory,
) {

    private val intermediateSourcesDirectory
        get() = File(project.buildDir, "intermediates/jisho/dictionaries")

    val destination = objectFactory.fileProperty().convention {
        File(project.buildDir, "generated/jisho/database/jisho.sqlite")
    }

    internal fun registerTasks() {
        val downloadTask = project.tasks.register("downloadJishoSources", DownloadFuriganaDictionariesTask::class.java) {
            outputDirectory.set(intermediateSourcesDirectory)
            group = JishoPlugin.GROUP
        }
        val prepareTask = project.tasks.register("prepareJishoSources", InflateDictionariesTask::class.java) {
            outputDirectory.set(intermediateSourcesDirectory)
            group = JishoPlugin.GROUP
            dependsOn(downloadTask)
        }
        val prepopulateTask = project.tasks.register("prepopulateJishoDatabase", PrepopulateTask::class.java) {
            sourcesDirectory.set(intermediateSourcesDirectory)
            databaseOutputFile.set(destination)
            group = JishoPlugin.GROUP
            dependsOn(prepareTask)
        }

        project.tasks
            .filter { task -> task.name.startsWith("assemble") }
            .forEach { assembleTask -> assembleTask.dependsOn(prepopulateTask) }
    }
}