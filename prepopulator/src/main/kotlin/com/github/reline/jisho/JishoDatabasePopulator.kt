package com.github.reline.jisho

import com.github.reline.jisho.jmdict.defaultJmdictClient
import com.github.reline.jisho.tasks.JishoDownloadTask
import com.github.reline.jisho.tasks.JishoPopulateTask
import com.github.reline.jisho.tasks.GzipResourceExtractionTask
import com.github.reline.jisho.tasks.ZipResourceExtractionTask
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
        )
        downloadTask.configure {
            group = jisho
            this.jmdictVersion.set(jmdictVersion)
            outputDir.set(File(cacheDir, "furigana"))
        }

        val dictionaries = File(cacheDir, "dictionaries")
        val extractJMdict = project.tasks.register("extractJMdict", GzipResourceExtractionTask::class.java) {
            fromResource(File("JMdict_e.xml.gz"))
            into(dictionaries.resolve("JMdict_e.xml"))
        }

        val extractJMnedict = project.tasks.register("extractJMnedict", GzipResourceExtractionTask::class.java) {
            fromResource(File("JMnedict.xml.gz"))
            into(dictionaries.resolve("JMnedict.xml"))
        }

        project.tasks.register("extractEdict2", GzipResourceExtractionTask::class.java) {
            fromResource(File("edict2.gz"))
            into(dictionaries.resolve("edict2"))
        }

        project.tasks.register("extractEnamdict", GzipResourceExtractionTask::class.java) {
            fromResource(File("enamdict.gz"))
            into(dictionaries.resolve("enamdict"))
        }

        val extractKanjidic2 = project.tasks.register("extractKanjidic2", GzipResourceExtractionTask::class.java) {
            fromResource(File("kanjidic2.xml.gz"))
            into(dictionaries.resolve("kanjidic2.xml"))
        }

        val extractRadicals = project.tasks.register("extractRadicals", ZipResourceExtractionTask::class.java) {
            fromResource(File("kradzip.zip"))
            into(cacheDir.resolve("radicals"))
        }

        val populateTask = project.tasks.register("populateJishoDatabase", JishoPopulateTask::class.java) {
            group = jisho
            description = "Generate prepopulated database with Jisho sources"
            jmdict.set(extractJMdict.get().outputFile)
            jmdictFurigana.set { downloadTask.get().outputDir.file("JmdictFurigana.json").get().asFile }
            jmnedict.set(extractJMnedict.get().outputFile)
            jmnedictFurigana.set { downloadTask.get().outputDir.file("JmnedictFurigana.json").get().asFile }
            kanji.from(extractKanjidic2)
            val radicals = extractRadicals.get().outputDirectory
            radicalKanjiMappings.from(radicals.files("radkfile", "radkfile2", "radkfilex"))
            kanjiRadicalMappings.from(radicals.files("kradfile", "kradfile2"))
            databaseOutputFile.set(File(cacheDir, "jisho.sqlite"))
        }

        project.tasks.register("copyJishoDatabase", Copy::class.java) {
            from(populateTask)
            into(destination)
        }
    }
}