package com.github.reline.jisho.jmdict

import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Streaming

/**
 * See [GitHub REST API documentation](https://docs.github.com/en/rest)
 */
interface GithubReleasesApi {
    /**
     * [Get the latest release](https://docs.github.com/en/rest/releases/releases#get-the-latest-release)
     */
    @GET("repos/{owner}/{repo}/releases/latest")
    @Headers("Accept: ${MimeType.GithubJson}")
    suspend fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): GithubRelease

    /**
     * [Get a release by tag name](https://docs.github.com/en/rest/releases/releases#get-a-release-by-tag-name)
     */
    @GET("repos/{owner}/{repo}/releases/tags/{tag}")
    @Headers("Accept: ${MimeType.GithubJson}")
    suspend fun getRelease(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("tag") tag: String,
    ): GithubRelease

    /**
     * [Get a release asset](https://docs.github.com/en/rest/releases/assets#get-a-release-asset)
     */
    @GET("repos/{owner}/{repo}/releases/assets/{asset_id}")
    @Streaming
    @Headers("Accept: ${MimeType.OctetStream}")
    suspend fun getReleaseAsset(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("asset_id") assetId: Int,
    ): ResponseBody
}

private const val GITHUB_API_BASE_URL = "https://api.github.com"

private val logger by lazy { LoggerFactory.getLogger("http-logger") }

fun GithubReleasesApi(githubToken: String?): GithubReleasesApi {
    val loggingInterceptor = HttpLoggingInterceptor { logger.debug(it) }.apply {
        redactHeader(Authorization)
        setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }
    val okHttpClient = OkHttpClient.Builder()
        .dispatcher(Dispatcher().apply { maxRequestsPerHost = 1 })
        .addInterceptor(loggingInterceptor)
        .addInterceptor(BearerAuthorizationInterceptor(githubToken))
        .addInterceptor(RateLimitInterceptor())
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl(GITHUB_API_BASE_URL)
        // todo: replace with kotlin serialization
        .addConverterFactory(MoshiConverterFactory.create())
        .callFactory(okHttpClient)
        .build()
    return retrofit.create(GithubReleasesApi::class.java)
}
