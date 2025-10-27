package com.github.reline.jisho

fun RepositoryHandler.jisho() {
    atilika()
}

pluginManagement {
    repositories {
        jisho()
    }
}

dependencyResolutionManagement {
    repositories {
        jisho()
    }
}
