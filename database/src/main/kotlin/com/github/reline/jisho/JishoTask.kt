package com.github.reline.jisho

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class JishoTask : DefaultTask() {
    @get:OutputFile
    abstract val destination: RegularFileProperty

    @TaskAction
    fun prepopulate() {
        val file = destination.get().asFile
        file.delete()
        file.parentFile.mkdirs()
        file.writeText("Hello!")
    }
}