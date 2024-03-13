package com.github.reline.jisho

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Nested
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class JishoExtension @Inject constructor(objectFactory: ObjectFactory) {
    @get:Nested
    abstract val database: JishoDatabasePopulator

    /**
     * Allow authenticated requests for a higher rate limit
     */
    val githubToken = objectFactory.property<String>()

    val jmdictVersion = objectFactory.property<String>()

    fun jmdict(version: Provider<String>) {
        jmdictVersion.set(version)
    }

    fun jmdict(version: String) {
        jmdictVersion.set(version)
    }

    fun database(action: Action<JishoDatabasePopulator>) {
        action.execute(database)
    }
}
