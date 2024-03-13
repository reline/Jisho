package com.github.reline.jisho

import com.github.reline.jisho.jmdict.defaultJmdictClient
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import java.io.File
import javax.inject.Inject

abstract class JishoDatabasePopulator @Inject constructor(
    private val project: Project,
    objectFactory: ObjectFactory,
) {

    private val buildDir: File
        get() = project.layout.buildDirectory.get().asFile

    private val cacheDir
        get() = File(project.gradle.gradleUserHomeDir, "jisho")

    val destination: RegularFileProperty = objectFactory.fileProperty().convention {
        File(buildDir, "generated/jisho/database/jisho.sqlite")
    }

    internal fun registerTasks(githubToken: Property<String>, jmdictVersion: Property<String>) {
        val jmdictClient = defaultJmdictClient(githubToken.orNull)
        val downloadTask = project.tasks.register(
            "downloadJishoSources",
            JishoDownloadTask::class.java,
            jmdictClient,
        )
        downloadTask.configure {
            // todo: invalidate task when github token changes
            this.jmdictVersion.set(jmdictVersion)
            outputDir.set(File(cacheDir, "furigana"))
        }

        val prepareTask = project.tasks.register("prepareJishoSources", JishoExtractionTask::class.java) {
            outputDirectory.set(File(cacheDir, "dictionaries"))
        }

        val populateTask = project.tasks.register("populateJishoDatabase", JishoPopulateTask::class.java) {
            dictionarySourcesDirectory.set(prepareTask.get().outputDirectory)
            furiganaSourcesDirectory.set(downloadTask.get().outputDir)
            databaseOutputFile.set(File(cacheDir, "jisho.sqlite"))
            dependsOn(downloadTask)
            dependsOn(prepareTask)
        }

        val copyTask = project.tasks.register("copyJishoDatabase", Copy::class.java) {
            from(populateTask.get().databaseOutputFile)
            into(destination)
            dependsOn(populateTask)
        }

        project.tasks.register("prepopulateJishoDatabase") {
            val tasks = listOf(downloadTask, prepareTask, populateTask, copyTask)
            tasks.forEachIndexed { i, task ->
                tasks.getOrNull(i + 1)?.get()?.mustRunAfter(task)
            }
            dependsOn(tasks)
        }
    }
}