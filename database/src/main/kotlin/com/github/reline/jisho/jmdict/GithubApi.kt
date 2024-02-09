package com.github.reline.jisho.jmdict

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Streaming

private const val DEFAULT_OWNER = "Doublevil"
private const val DEFAULT_REPO = "JmdictFurigana"

interface GithubApi {
    @GET("repos/{owner}/{repo}/releases/latest")
    @Headers("Accept: application/vnd.github+json")
    suspend fun getLatestRelease(
        @Path("owner") owner: String = DEFAULT_OWNER,
        @Path("repo") repo: String = DEFAULT_REPO,
    ): GithubRelease

    @GET("repos/{owner}/{repo}/releases/tags/{tag}")
    @Headers("Accept: application/vnd.github+json")
    suspend fun getRelease(
        @Path("owner") owner: String = DEFAULT_OWNER,
        @Path("repo") repo: String = DEFAULT_REPO,
        @Path("tag") tag: String,
    ): GithubRelease

    @GET("repos/{owner}/{repo}/releases/assets/{asset_id}")
    @Streaming
    @Headers("Accept: application/octet-stream")
    suspend fun getReleaseAsset(
        @Path("owner") owner: String = DEFAULT_OWNER,
        @Path("repo") repo: String = DEFAULT_REPO,
        @Path("asset_id") assetId: Int,
    ): ResponseBody
}
