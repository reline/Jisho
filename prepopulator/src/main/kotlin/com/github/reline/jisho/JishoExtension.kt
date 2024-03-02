package com.github.reline.jisho

import org.gradle.api.Action
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Nested
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class JishoExtension @Inject constructor(objectFactory: ObjectFactory) {
    @get:Nested
    abstract val database: JishoDatabasePrepopulator

    // allow authenticated requests for a higher rate limit
    val githubToken = objectFactory.property<String>().convention(null as String?)

    val jmdictVersion = objectFactory.property<VersionConstraint>().convention(null as VersionConstraint?)

    fun database(action: Action<JishoDatabasePrepopulator>) {
        action.execute(database)
    }
}