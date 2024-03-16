package com.github.reline.jisho

import com.github.reline.jisho.jmdict.defaultJmdictClient
import com.github.reline.jisho.tasks.JishoDownloadTask
import com.github.reline.jisho.tasks.ResourceExtractionTask
import com.github.reline.jisho.tasks.gradleCachesDir
import com.github.reline.jisho.tasks.register
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.io.File
import javax.inject.Inject

abstract class JishoDatabasePopulator @Inject constructor(
    private val project: Project,
    objectFactory: ObjectFactory,
) {

    private val buildDir: File
        get() = project.layout.buildDirectory.get().asFile

    private val cacheDir
        get() = File(project.gradle.gradleCachesDir, "jisho")

    val destination: RegularFileProperty = objectFactory.fileProperty().convention {
        File(buildDir, "generated/jisho/database/jisho.sqlite")
    }

    internal fun registerTasks(githubToken: Property<String>, jmdictVersion: Property<String>) {
        // todo: consider using a factory to reduce gradle sync/config time
        val jmdictClient = defaultJmdictClient(githubToken.orNull)
        val downloadTask = project.tasks.register(
            "downloadJishoSources",
            JishoDownloadTask::class.java,
            jmdictClient,
        ) {
            this.jmdictVersion.set(jmdictVersion)
            outputDir.set(File(cacheDir, "furigana"))
        }

        val extractDir = File(cacheDir, "dictionaries")
        val extractJMdict = project.tasks.register("extractJMdict", ResourceExtractionTask::class.java) {
            fromResource(File("JMdict_e.xml.gz"))
            into(extractDir)
        }

        val extractJMnedict = project.tasks.register("extractJMnedict", ResourceExtractionTask::class.java) {
            fromResource(File("JMnedict.xml.gz"))
            into(extractDir)
        }

        val extractEdict2 = project.tasks.register("extractEdict2", ResourceExtractionTask::class.java) {
            fromResource(File("edict2.gz"))
            into(extractDir)
        }

        val extractEnamdict = project.tasks.register("extractEnamdict", ResourceExtractionTask::class.java) {
            fromResource(File("enamdict.gz"))
            into(extractDir)
        }

        val extractKanjidic2 = project.tasks.register("extractKanjidic2", ResourceExtractionTask::class.java) {
            fromResource(File("kanjidic2.xml.gz"))
            into(extractDir)
        }

        val extractKrad = project.tasks.register("extractKrad", ResourceExtractionTask::class.java) {
            fromResource(File("kradzip.zip"))
            into(extractDir)
        }

        val prepareTask = project.tasks.register("prepareJishoSources") {
            group = jisho
            dependsOn(
                extractJMdict,
                extractJMnedict,
                extractEdict2,
                extractEnamdict,
                extractKanjidic2,
                extractKrad,
            )
        }

//        val populateTask = project.tasks.register("populateJishoDatabase", JishoPopulateTask::class.java) {
//            dictionarySources.from(prepareTask.get().outputFiles)
//            furiganaSources.from(downloadTask.get().outputFiles)
//            databaseOutputFile.set(File(cacheDir, "jisho.sqlite"))
//        }
//
//        val copyTask = project.tasks.register("copyJishoDatabase", Copy::class.java) {
//            from(populateTask.get().databaseOutputFile)
//            into(destination)
//        }
//
//        project.tasks.register("prepopulateJishoDatabase") {
//            group = jisho
//            description = "Generate prepopulated database with Jisho sources"
//            val tasks = listOf(downloadTask, prepareTask, populateTask, copyTask)
//            tasks.forEachIndexed { i, task ->
//                tasks.getOrNull(i + 1)?.get()?.mustRunAfter(task)
//            }
//            dependsOn(tasks)
//        }
    }
}