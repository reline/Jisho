package com.github.reline.jisho.tasks

import org.gradle.api.Task
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskContainer
import java.io.File

fun <T : Task> TaskContainer.register(
    name: String,
    type: Class<T>,
    vararg constructorArgs: Any,
    action: T.() -> Unit,
) = register(name, type, constructorArgs).also { it.configure(action) }

val Gradle.gradleCachesDir
    get() = File(gradleUserHomeDir, "caches")
