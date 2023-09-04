//import com.squareup.moshi.Moshi
//import groovy.json.JsonSlurper
//import okio.buffer
//import java.net.URL
//import okio.sink
//import okio.source
//
///**
// * Incremental builds:
// *
// * Prepare monash sources
// * Download furigana sources
// */
//
//val fetchFuriganaDictionaries by tasks.register("fetchFuriganaDictionaries") {
//    data class GithubAsset(var name: String = "", var browser_download_url: String? = null)
//    data class GithubRelease(var assets: List<GithubAsset> = emptyList())
//    doLast {
//        // https://docs.github.com/en/rest/reference/repos#get-a-release-by-tag-name
//        // todo: use version from .toml
//        val url = "https://api.github.com/repos/Doublevil/JmdictFurigana/releases/tags/2.3.0+2023-08-25"
//        val release = JsonSlurper().parseText(URL(url).openStream().source().buffer().readUtf8()) as Map<String, Any>
//        val assets = release["assets"] as List<Map<String, String>>
//        assets.forEach { asset ->
//            val (name, browser_download_url) = GithubAsset(asset.getOrDefault("name", ""), asset["browser_download_url"])
//            if (name.endsWith(".json") && browser_download_url != null) {
//
//                val dictionary = layout.buildDirectory.file(name).get().asFile
//                if (dictionary.exists()) return@forEach // fixme: use gradle caching
//                dictionary.createNewFile()
//                dictionary.outputStream().sink().buffer()
//                    .writeAll(URL(browser_download_url).openStream().source())
//            }
//        }
//    }
//}
//
//val prepareMonashDictionaries by tasks.register("prepareMonashDictionaries") {
//    doLast {
//
//    }
//}
//
//abstract class DictionarySourcesToDatabaseTask : DefaultTask() {
//    @get:OutputFile
//    abstract val destination: RegularFileProperty
//
//    @TaskAction
//    fun prepopulate() {
//        val file = destination.get().asFile
//        file.delete()
//        file.parentFile.mkdirs()
//        file.writeText("Hello!")
//    }
//}
//
//val jisho by tasks.registering(DictionarySourcesToDatabaseTask::class) {
//    val databaseFile = destination
//    dependsOn(fetchFuriganaDictionaries)
//    doLast {
//        val file = databaseFile.get().asFile
//        println("Wrote ${file.absolutePath}")
//    }
//}
//
//// fixme
//tasks.named("preBuild") {
//    dependsOn(jisho)
//}
//tasks.named("assemble") {
//    finalizedBy(jisho)
//}
