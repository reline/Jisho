package com.github.reline.jisho

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Nested
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class JishoExtension @Inject constructor(objectFactory: ObjectFactory) {
    @get:Nested
    abstract val database: JishoDatabase

    // allow authenticated requests for a higher rate limit
    val githubToken = objectFactory.property<String>().convention(null as String?)

    fun database(action: Action<JishoDatabase>) {
        action.execute(database)
    }
}