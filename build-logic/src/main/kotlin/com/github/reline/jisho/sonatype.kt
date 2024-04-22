package com.github.reline.jisho

import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

fun RepositoryHandler.sonatypeSnapshots() = maven {
    url = URI("https://oss.sonatype.org/content/repositories/snapshots")
    mavenContent {
        snapshotsOnly()
    }
}
