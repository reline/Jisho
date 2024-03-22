package com.github.reline.jisho

import org.gradle.api.invocation.Gradle
import java.io.File

val Gradle.gradleCachesDir
    get() = File(gradleUserHomeDir, "caches")
