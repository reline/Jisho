package com.github.reline.jisho

import com.github.reline.jisho.artifactrepository.atilika
import com.github.reline.jisho.artifactrepository.edrdg
import com.github.reline.jisho.artifactrepository.googleContent
import com.github.reline.jisho.artifactrepository.jmdictfurigana
import com.github.reline.jisho.artifactrepository.sonatypeSnapshots

fun RepositoryHandler.jisho() {
    jmdictfurigana(providers.environmentVariable("GITHUB_TOKEN"))
    atilika()
    edrdg()

    exclusiveContent {
        forRepository { sonatypeSnapshots() }
        filter { includeGroup("com.tickaroo.tikxml") }
    }

    // todo: consider using `releasesOnly` with other repositories
    sonatypeSnapshots()

    googleContent()
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
