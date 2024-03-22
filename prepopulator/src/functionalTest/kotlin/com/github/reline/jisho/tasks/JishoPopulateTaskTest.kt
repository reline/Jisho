package com.github.reline.jisho.tasks

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class JishoPopulateTaskTest {

    private lateinit var testProjectDir: File
    private lateinit var settingsFile: File
    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        testProjectDir = Files.createTempDirectory("tmpDir").toFile()

        settingsFile = File(testProjectDir, "settings.gradle.kts")
        settingsFile.appendText("""
            pluginManagement {
                repositories {
                    gradlePluginPortal()
                    google()
                    mavenCentral()
                }
            }

        """.trimIndent())

        buildFile = File(testProjectDir, "build.gradle.kts")
        buildFile.appendText("""
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.9.10"
                id("com.github.reline.jisho.prepopulator")
            }
            
        """.trimIndent())
    }

    @AfterEach
    fun teardown() {
        Files.walk(testProjectDir.toPath()).use {
            it.filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }

    @Test
    fun smokeTest() {
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("populateJishoDatabase")
            .withPluginClasspath()
            .build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":populateJishoDatabase")?.outcome)
    }
}
