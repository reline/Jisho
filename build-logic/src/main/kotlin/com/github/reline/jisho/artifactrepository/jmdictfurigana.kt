package com.github.reline.jisho.artifactrepository

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.credentials.HttpHeaderCredentials
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.credentials
import java.net.URI

fun RepositoryHandler.jmdictfurigana(githubToken: Provider<String>) = ivy {
    name = "JmdictFurigana"
    url = URI("https://github.com")
    credentials(HttpHeaderCredentials::class) {
        name = "Authorization"
        value = "Bearer ${githubToken.orNull}"
    }
    patternLayout {
        artifact("[organisation]/[module]/releases/download/[revision]/[classifier](.[ext])")
    }
    metadataSources {
        artifact()
    }
    content {
        includeModule("Doublevil", "JmdictFurigana")
    }
}
