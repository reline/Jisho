package com.github.reline.jisho

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class PrepopulateTask : DefaultTask() {
    @get:OutputFile
    abstract val databaseOutputFile: RegularFileProperty
    override fun getDescription() = "Generate ${databaseOutputFile.asFile.get().name} file"

    // todo: add sources

    @TaskAction
    fun prepopulate() {
        val file = databaseOutputFile.get().asFile
        file.delete()
        file.parentFile.mkdirs()
        file.writeText("Hello!")
    }
}