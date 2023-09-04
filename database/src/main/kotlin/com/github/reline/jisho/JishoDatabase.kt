package com.github.reline.jisho

import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import java.io.File
import javax.inject.Inject

abstract class JishoDatabase @Inject constructor(
    val project: Project,
) {

    @get:OutputFile
    abstract val destination: RegularFileProperty

    private val intermediateSourcesDirectory
        get() = File(project.buildDir, "intermediates/jisho/dictionaries")
    internal fun registerTasks() {
        val downloadTask = project.tasks.register("downloadJishoSources", DownloadFuriganaDictionariesTask::class.java) {
            outputDirectory.set(intermediateSourcesDirectory)
        }
        val prepareTask = project.tasks.register("prepareJishoSources", InflateDictionariesTask::class.java) {
            outputDirectory.set(intermediateSourcesDirectory)
        }
        val prepopulateTask = project.tasks.register("prepopulateJishoDatabase", PrepopulateTask::class.java) {
            databaseOutputFile.set(destination)
        }
        prepopulateTask.configure {
            dependsOn(downloadTask)
            dependsOn(prepareTask)
        }
        project.tasks.named("preBuild") {
            dependsOn(prepopulateTask)
        }
        project.tasks.named("assemble") {
            finalizedBy(prepopulateTask)
        }
    }
}