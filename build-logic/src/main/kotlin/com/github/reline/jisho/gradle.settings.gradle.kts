package com.github.reline.jisho

fun RepositoryHandler.jisho() {
    atilika()

    exclusiveContent {
        forRepository { jitpack() }
        filter { includeGroup("com.github.reline") }
    }
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
