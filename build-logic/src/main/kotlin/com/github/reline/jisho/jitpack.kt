package com.github.reline.jisho

import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

fun RepositoryHandler.jitpack() = maven {
    url = URI("https://www.jitpack.io")
}
