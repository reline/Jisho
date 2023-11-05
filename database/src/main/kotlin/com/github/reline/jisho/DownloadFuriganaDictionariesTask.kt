package com.github.reline.jisho

import groovy.json.JsonSlurper
import okio.IOException
import okio.buffer
import okio.sink
import okio.source
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.net.URL

abstract class DownloadFuriganaDictionariesTask : DefaultTask() {

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    override fun getDescription() = "Download furigana dictionary files"

    private val versionCatalog get() = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

    @TaskAction
    fun download() {
        data class GithubAsset(var name: String = "", var browser_download_url: String? = null)
        data class GithubRelease(var assets: List<GithubAsset> = emptyList())

        outputDirectory.get().asFile.mkdirs()

        // https://docs.github.com/en/rest/reference/repos#get-a-release-by-tag-name
        val version = versionCatalog.findVersion("jmdictfurigana").get().requiredVersion
        // todo: move url to toml?
        val url = "https://api.github.com/repos/Doublevil/JmdictFurigana/releases/tags/${version}"
        val release =
            try {
                URL(url).openStream().source().buffer().use {
                    JsonSlurper().parseText(it.readUtf8()) as Map<String, Any>
                }
            } catch (e: IOException) {
                throw RuntimeException("Failed to download $url", e)
            }

        val assets = release["assets"] as List<Map<String, String>>
        assets.forEach { asset ->
            val (name, browser_download_url) = GithubAsset(asset.getOrDefault("name", ""), asset["browser_download_url"])
            if (name.endsWith(".json") && browser_download_url != null) {
                val dictionary = outputDirectory.file(name).get().asFile
                dictionary.createNewFile()
                try {
                    dictionary.outputStream().sink().buffer().use { sink ->
                        URL(browser_download_url).openStream().source().buffer().use { source ->
                            source.readAll(sink)
                        }
                    }
                } catch (e: IOException) {
                    throw RuntimeException("Failed to download $browser_download_url", e)
                }
            }
        }
    }
}