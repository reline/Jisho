package com.github.reline.jisho

import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

fun RepositoryHandler.atilika() = maven {
    url = URI("https://www.atilika.org/nexus/content/repositories/atilika")
    content {
        // this repository *only* contains artifacts in "org.atilika"
        includeGroupByRegex("org\\.atilika.*")
    }
}
