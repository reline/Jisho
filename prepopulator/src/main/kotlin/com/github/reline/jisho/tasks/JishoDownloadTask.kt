package com.github.reline.jisho.tasks

import com.github.reline.jisho.jmdict.JmdictClient
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okio.Path.Companion.toOkioPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

/**
 * TODO: crazy ideas!
 *
 * Objectives:
 * 1. Offload artifact caching to gradle
 * 2. Simplify artifact transformation (e.g. unarchiving gzip and zip files)
 * 3. Uniform approach to artifact versioning and resolution
 *
 * Methods:
 * 1. Try to use a custom ivy resolver first since we're just downloading simple files.
 * https://gist.github.com/ysb33r/9f95bab338b912c45986
 * 2. Start a local server (conditionally) which handles HEAD requests and forwards dependency
 * resolution to an input URL. An extension would be provided to easily add a new "forward HEAD"
 * repo which actually points to the local server. Explicit repositories could be done as a first
 * milestone and perhaps even a custom configuration.
 * e.g.
 * ```
 * repositories {
 *     github()
 * }
 *
 * dependencies {
 *     githubAsset("org:repo:version-asset") // or "org.repo:asset:version"
 * }
 * ```
 * The GitHub resolver would actually need to perform a request to repos/{owner}/{repo}/releases/tags/{tag}
 * and match the asset name to find the asset ID, followed by actually downloading the asset from
 * repos/{owner}/{repo}/releases/assets/{asset_id}.
 * The initial HEAD request will likely require a request to repos/{owner}/{repo}/releases/tags/{tag}
 * as well to check if the asset has changed. The asset information could possibly be hashed to
 * determine this? Optimally the actual asset resolution request would use the response from the
 * GET performed when handling the HEAD request so that another request would not have to be performed.
 * Likely it would automatically be handled by using a single http client with a response cache.
 * The HEAD request could possibly use the http client cache as well directly on the asset endpoint.
 * The response headers would indicate if the response was cached and therefore if the asset has
 * changed (provided the appropriate cache request headers).
 */
abstract class JishoDownloadTask @Inject constructor(
    private val jmdictClient: Provider<JmdictClient>,
) : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val jmdictVersion: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun download() = runBlocking {
        // todo: inject gradle log level & format; Download <url>, took 83 ms (2.27 kB)
        val destination = outputDir.get().asFile.toOkioPath()
        val client = jmdictClient.get()
        val version = jmdictVersion.orNull
        withTimeout(timeout.orNull?.toKotlinDuration() ?: Duration.INFINITE) {
            client.downloadDictionaries(destination, version)
        }
    }
}