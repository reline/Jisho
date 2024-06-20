package com.github.reline.jisho

import org.gradle.testkit.runner.GradleRunner
import java.io.File

import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JishoPluginTest {
    // todo: https://github.com/junit-team/junit5/issues/2811
    // @TempDir
    private lateinit var testProjectDir: File
    private val testBuildDir get() = File(testProjectDir, "build")

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

    @Disabled("Feature under test is disabled")
    @Test
    fun prepopulateDatabaseCustomDestinationTest() {
        val destination = "jisho.sqlite"
        buildFile.appendText("""
            jisho {
                database {
                    destination.set(layout.buildDirectory.file("$destination").get())
                }
            }
        """.trimIndent())
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("populateJishoDatabase", "--info")
            .withPluginClasspath()
            .build()
        assertTrue(result.output.contains("Generated build/$destination"))
        assertTrue(File(testBuildDir, destination).exists())
        assertEquals(SUCCESS, result.task(":prepopulateJishoDatabase")?.outcome)
    }
}