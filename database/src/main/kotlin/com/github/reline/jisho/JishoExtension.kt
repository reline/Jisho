package com.github.reline.jisho

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

abstract class JishoExtension {
    @get:Nested
    abstract val database: JishoDatabase

    fun database(action: Action<JishoDatabase>) {
        action.execute(database)
    }
}