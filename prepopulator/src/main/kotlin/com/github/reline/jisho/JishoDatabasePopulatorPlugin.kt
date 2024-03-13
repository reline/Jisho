package com.github.reline.jisho

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class JishoDatabasePopulatorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = with(project.extensions) {
            findByType(JishoExtension::class.java) ?: create(JISHO, JishoExtension::class.java)
        }
        project.afterEvaluate {
            extension.database.registerTasks(extension.githubToken, extension.jmdictVersion)
        }
    }

    internal companion object {
        private const val JISHO = "jisho"
        const val GROUP = JISHO
    }
}
