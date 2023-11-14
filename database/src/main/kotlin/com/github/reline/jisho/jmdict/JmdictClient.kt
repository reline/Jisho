package com.github.reline.jisho.jmdict

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okio.buffer
import okio.sink
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.IOException

// todo: unit test

private const val GITHUB_API_BASE_URL = "https://api.github.com"

fun defaultJmdictClient(bearerToken: String?): JmdictClient {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(GithubAuthenticator(bearerToken))
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

    /**
     * @param destination The destination to download the Jmdict assets. Must be a directory.
     */
    @Throws(IOException::class)
    fun downloadLatestAssets(destination: File) = runBlocking {
        require(destination.isDirectory) { "$destination is not a directory" }
        if (!destination.exists()) {
            check(destination.mkdirs()) {
                "Cannot create directory at $destination"
            }
        }

        githubApi.getLatestRelease().assets.forEach { asset ->
            // skip assets that aren't json
            if (!asset.name.endsWith(".json")) return@forEach

            val dictionary = File(destination, asset.name)
            if (dictionary.exists()) {
                check(dictionary.delete()) {
                    "Cannot delete existing file at $dictionary"
                }
            }
            check(dictionary.createNewFile()) {
                "Cannot create file at $dictionary"
            }

            val response = githubApi.getReleaseAsset(assetId = asset.id)
            dictionary.outputStream().sink().buffer().use { sink ->
                response.source().buffer.use { source ->
                    source.readAll(sink)
                }
            }
        }
    }
}
