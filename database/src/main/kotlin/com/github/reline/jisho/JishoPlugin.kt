package com.github.reline.jisho

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class JishoPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("jisho", JishoExtension::class.java)
        project.afterEvaluate {
            extension.database.registerTasks()
        }
    }
}