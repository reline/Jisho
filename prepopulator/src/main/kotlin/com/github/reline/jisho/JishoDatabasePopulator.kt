package com.github.reline.jisho

import com.github.reline.jisho.jmdict.defaultJmdictClient
import com.github.reline.jisho.tasks.JishoDownloadTask
import com.github.reline.jisho.tasks.JishoPopulateTask
import com.github.reline.jisho.tasks.ResourceExtractionTask
import com.github.reline.jisho.tasks.gradleCachesDir
import com.github.reline.jisho.tasks.register
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Copy
import java.io.File
import javax.inject.Inject

abstract class JishoDatabasePopulator @Inject constructor(
    private val project: Project,
) {

    private val buildDir: DirectoryProperty
        get() = project.layout.buildDirectory

    private val cacheDir
        get() = File(project.gradle.gradleCachesDir, "jisho")

    val destination: Provider<RegularFile> get() =
        buildDir.file("generated/jisho/database/jisho.sqlite")

    internal fun registerTasks(githubToken: Property<String>, jmdictVersion: Property<String>) {
        // todo: consider using a factory to reduce gradle sync/config time
        //  ...or use lazy with dagger
        val jmdictClient = defaultJmdictClient(githubToken.orNull)
        val downloadTask = project.tasks.register(
            "downloadJishoSources",
            JishoDownloadTask::class.java,
            jmdictClient,
        ) {
            group = jisho
            this.jmdictVersion.set(jmdictVersion)
            outputDir.set(File(cacheDir, "furigana"))
        }

        val dictionaries = File(cacheDir, "dictionaries")
        val extractJMdict = project.tasks.register("extractJMdict", ResourceExtractionTask::class.java) {
            fromResource(File("JMdict_e.xml.gz"))
            into(dictionaries)
        }

        val extractJMnedict = project.tasks.register("extractJMnedict", ResourceExtractionTask::class.java) {
            fromResource(File("JMnedict.xml.gz"))
            into(dictionaries)
        }

        val extractEdict2 = project.tasks.register("extractEdict2", ResourceExtractionTask::class.java) {
            fromResource(File("edict2.gz"))
            into(dictionaries)
        }

        val extractEnamdict = project.tasks.register("extractEnamdict", ResourceExtractionTask::class.java) {
            fromResource(File("enamdict.gz"))
            into(dictionaries)
        }

        val extractKanjidic2 = project.tasks.register("extractKanjidic2", ResourceExtractionTask::class.java) {
            fromResource(File("kanjidic2.xml.gz"))
            into(File(cacheDir, "kanji"))
        }

        val extractKrad = project.tasks.register("extractKrad", ResourceExtractionTask::class.java) {
            fromResource(File("kradzip.zip"))
            into(File(cacheDir, "radicals"))
        }

        val populateTask = project.tasks.register("populateJishoDatabase", JishoPopulateTask::class.java) {
            group = jisho
            description = "Generate prepopulated database with Jisho sources"
            dictionarySources.from(extractJMdict, extractJMnedict)
            furiganaSources.from(downloadTask)
            kanjiSources.from(extractKanjidic2)
            radicalSources.from(extractKrad)
            databaseOutputFile.set(File(cacheDir, "jisho.sqlite"))
        }

        project.tasks.register("copyJishoDatabase", Copy::class.java) {
            from(populateTask)
            into(destination)
        }
    }
}