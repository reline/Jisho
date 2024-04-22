package com.github.reline.jisho

fun RepositoryHandler.jisho() {
    atilika()

    exclusiveContent {
        forRepository { jitpack() }
        filter { includeGroup("com.github.reline") }
    }

    exclusiveContent {
        forRepository { sonatypeSnapshots() }
        filter { includeGroup("com.tickaroo.tikxml") }
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
