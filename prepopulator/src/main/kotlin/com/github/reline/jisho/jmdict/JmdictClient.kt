package com.github.reline.jisho.jmdict

import com.github.reline.jisho.ensureDirsCreated
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.buffer
import okio.sink
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.IOException

// todo: unit test

private const val GITHUB_API_BASE_URL = "https://api.github.com"

private val logger by lazy { LoggerFactory.getLogger("http-logger") }

fun defaultJmdictClient(githubToken: String?): JmdictClient {
    val loggingInterceptor = HttpLoggingInterceptor { logger.debug(it) }.apply {
        redactHeader(AUTHORIZATION)
        setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(BearerAuthorizationInterceptor(githubToken))
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl(GITHUB_API_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .callFactory(okHttpClient)
        .build()
    val githubApi = retrofit.create(GithubApi::class.java)
    return JmdictClient(githubApi)
}

class JmdictClient(private val githubApi: GithubApi) {

    @Throws(IOException::class)
    suspend fun downloadAssets(version: String?, destination: File) {
        val release = if (version == null) {
            githubApi.getLatestRelease()
        } else {
            githubApi.getRelease(tag = version)
        }
        saveAssets(release.assets, destination)
    }

    private suspend fun saveAssets(assets: List<GithubAsset>, destination: File) = coroutineScope {
        destination.ensureDirsCreated()

        // skip assets that aren't json
        assets.filter { it.name.endsWith(".json") }
            .map { asset ->
                val dictionary = File(destination, asset.name)
                if (dictionary.exists()) {
                    check(dictionary.delete()) {
                        "Cannot delete existing file at $dictionary"
                    }
                }
                check(dictionary.createNewFile()) {
                    "Cannot create file at $dictionary"
                }

                async {
                    // todo: Download <url>, took 83 ms (2.27 kB)
                    val response = githubApi.getReleaseAsset(assetId = asset.id)
                    dictionary.outputStream().sink().buffer().use { sink ->
                        response.source().buffer.use { source ->
                            source.readAll(sink)
                        }
                    }
                }
            }.joinAll()
    }
}
