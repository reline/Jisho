package com.github.reline.jisho

import org.gradle.api.Plugin
import org.gradle.api.Project

internal const val jisho = "jisho"

abstract class JishoDatabasePopulatorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = with(project.extensions) {
            findByType(JishoExtension::class.java) ?: create(jisho, JishoExtension::class.java)
        }
        project.afterEvaluate {
            extension.database.registerTasks(extension.githubToken, extension.jmdictVersion)
        }
    }
}
