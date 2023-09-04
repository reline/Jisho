package com.github.reline.jisho

import groovy.json.JsonSlurper
import okio.buffer
import okio.sink
import okio.source
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.net.URL

abstract class DownloadFuriganaDictionariesTask : DefaultTask() {

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    override fun getDescription() = "Download furigana dictionary files"

    @TaskAction
    fun download() {
        data class GithubAsset(var name: String = "", var browser_download_url: String? = null)
        data class GithubRelease(var assets: List<GithubAsset> = emptyList())

        outputDirectory.get().asFile.mkdirs()

        // https://docs.github.com/en/rest/reference/repos#get-a-release-by-tag-name
        // todo: use version from .toml
        val url = "https://api.github.com/repos/Doublevil/JmdictFurigana/releases/tags/2.3.0+2023-08-25"
        val release = JsonSlurper().parseText(URL(url).openStream().source().buffer().readUtf8()) as Map<String, Any>
        val assets = release["assets"] as List<Map<String, String>>
        assets.forEach { asset ->
            val (name, browser_download_url) = GithubAsset(asset.getOrDefault("name", ""), asset["browser_download_url"])
            if (name.endsWith(".json") && browser_download_url != null) {
                val dictionary = outputDirectory.file(name).get().asFile
                if (dictionary.exists()) return@forEach // fixme: use gradle caching
                dictionary.createNewFile()
                dictionary.outputStream().sink().buffer()
                    .writeAll(URL(browser_download_url).openStream().source().buffer())
            }
        }
    }
}