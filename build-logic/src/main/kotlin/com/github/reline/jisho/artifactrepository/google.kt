package com.github.reline.jisho.artifactrepository

import org.gradle.api.artifacts.dsl.RepositoryHandler

fun RepositoryHandler.googleContent() = google {
    content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
    }
}
