package com.github.reline.jisho.jmdict

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubRelease(val assets: List<GithubAsset>)

@JsonClass(generateAdapter = true)
data class GithubAsset(
    val id: Int,
    val name: String,
)
