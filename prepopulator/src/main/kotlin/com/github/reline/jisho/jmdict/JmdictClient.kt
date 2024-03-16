package com.github.reline.jisho.jmdict

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path
import java.io.File
import java.io.IOException

fun defaultJmdictClient(githubToken: String?): JmdictClient {
    val githubApi = GithubReleasesApi(githubToken)
    return JmdictClient(githubApi)
}

class JmdictClient(
    private val githubApi: GithubReleasesApi,
    private val fileSystem: FileSystem = FileSystem.SYSTEM,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    companion object {
        private const val OWNER = "Doublevil"
        private const val REPO = "JmdictFurigana"
    }

    @Throws(IOException::class)
    suspend fun downloadDictionaries(
        destination: Path,
        version: String? = null,
    ) = withContext(ioDispatcher) {
        val release = if (version == null) {
            githubApi.getLatestRelease(owner = OWNER, repo = REPO)
        } else {
            githubApi.getRelease(owner = OWNER, repo = REPO, tag = version)
        }

        release.assets
            // only download json assets
            .filter { File(it.name).extension == "json" }
            .map { asset ->
                // fixme:
                //  Exception in thread "Daemon client event forwarder"
                //  Exception in thread "Daemon health stats" java.lang.OutOfMemoryError: Java heap space
                val response = githubApi.getReleaseAsset(
                    owner = OWNER,
                    repo = REPO,
                    assetId = asset.id
                )

                val file = destination/asset.name
                fileSystem.write(destination/asset.name) {
                    response.source().use { source ->
                        // todo: inquire about `writeAll(Source)`
                        source.readAll(this)
                    }
                }
                return@map file
        }
    }
}
