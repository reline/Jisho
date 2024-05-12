package com.github.reline.jisho

fun RepositoryHandler.jisho() {
    google {
        content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("com\\.google.*")
            includeGroupByRegex("androidx.*")
        }
    }

    atilika()

    exclusiveContent {
        forRepository { sonatypeSnapshots() }
        filter { includeGroup("com.tickaroo.tikxml") }
    }

    mavenCentral()
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        jisho()
    }
}

dependencyResolutionManagement {
    repositories {
        jisho()
    }
}
