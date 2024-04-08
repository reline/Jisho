package com.github.reline.jisho

import com.github.reline.jisho.jmdict.defaultJmdictClient
import com.github.reline.jisho.tasks.Dictionary
import com.github.reline.jisho.tasks.JishoDownloadTask
import com.github.reline.jisho.tasks.JishoPopulateTask
import com.github.reline.jisho.tasks.GzipResourceExtractionTask
import com.github.reline.jisho.tasks.ZipResourceExtractionTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import javax.inject.Inject

abstract class JishoDatabasePopulator @Inject constructor(
    project: Project,
    private val projectLayout: ProjectLayout,
    private val objectFactory: ObjectFactory,
    private val providerFactory: ProviderFactory,
) {

    private val tasks = project.tasks

    private val buildDir: DirectoryProperty
        get() = projectLayout.buildDirectory

    private val intermediates
        get() = buildDir.dir("intermediates/jisho")

    // todo: functional test
    val destination = objectFactory.fileProperty().convention(
        buildDir.file("generated/jisho/jisho.sqlite")
    )

    internal fun registerTasks(githubToken: Property<String>, jmdictVersion: Property<String>) {
        val jmdictClient = providerFactory.provider { defaultJmdictClient(githubToken.orNull) }
        val downloadTask = tasks.register(
            "downloadJishoSources",
            JishoDownloadTask::class.java,
            jmdictClient,
        )
        downloadTask.configure {
            group = jisho
            this.jmdictVersion.set(jmdictVersion)
            outputDir.set(intermediates.map { it.dir("furigana") })
        }

        val extractJMdict = tasks.register("extractJMdict", GzipResourceExtractionTask::class.java) {
            resourceAssetPath.set("JMdict_e.xml.gz")
            outputFile.set(intermediates.map { it.file("JMdict_e.xml") })
        }

        val extractJMnedict = tasks.register("extractJMnedict", GzipResourceExtractionTask::class.java) {
            resourceAssetPath.set("JMnedict.xml.gz")
            outputFile.set(intermediates.map { it.file("JMnedict.xml") })
        }

        tasks.register("extractEdict2", GzipResourceExtractionTask::class.java) {
            resourceAssetPath.set("edict2.gz")
            outputFile.set(intermediates.map { it.file("edict2") })
        }

        tasks.register("extractEnamdict", GzipResourceExtractionTask::class.java) {
            resourceAssetPath.set("enamdict.gz")
            outputFile.set(intermediates.map { it.file("enamdict") })
        }

        val extractKanjidic2 = tasks.register("extractKanjidic2", GzipResourceExtractionTask::class.java) {
            resourceAssetPath.set("kanjidic2.xml.gz")
            outputFile.set(intermediates.map { it.file("kanjidic2.xml") })
        }

        val extractRadicals = tasks.register("extractRadicals", ZipResourceExtractionTask::class.java) {
            resourceAssetPath.set("kradzip.zip")
            outputDirectory.set(intermediates.map { it.dir("radicals") })
        }

        tasks.register("populateJishoDatabase", JishoPopulateTask::class.java) {
            group = jisho
            description = "Generate prepopulated database with Jisho sources"

            // fixme: download task is run twice during functional tests.
            //  maybe caching just needs to be enabled?
            dictionaries.add(
                extractJMdict.zip(downloadTask) { gzip, furigana ->
                    Dictionary(
                        definitions = gzip.outputFile.get().asFile,
                        okurigana = furigana.outputDir.get().asFile.resolve("JmdictFurigana.json"),
                    )
                }
            )

            dictionaries.add(
                extractJMnedict.zip(downloadTask) { gzip, furigana ->
                    Dictionary(
                        definitions = gzip.outputFile.get().asFile,
                        okurigana = furigana.outputDir.get().asFile.resolve("JmnedictFurigana.json"),
                    )
                }
            )

            kanji.from(extractKanjidic2)

//            val radk = listOf("radkfile", "radkfile2", "radkfilex").forEach { radicals.resolve(it) }
//            radicalKanjiMappings.from(files(radk) {
//                builtBy(extractRadicals)
//            })
//            val krad = listOf("kradfile", "kradfile2").forEach { radicals.resolve(it) }
//            kanjiRadicalMappings.from(files(krad) {
//                builtBy(extractRadicals)
//            })

            databaseOutputFile.set(destination)
        }
    }
}
