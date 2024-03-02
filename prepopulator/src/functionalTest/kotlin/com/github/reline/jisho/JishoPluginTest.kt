package com.github.reline.jisho

import org.gradle.testkit.runner.GradleRunner
import java.io.File

import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path

class JishoPluginTest {
    // https://github.com/junit-team/junit5/issues/2811
    // @TempDir
    lateinit var testProjectDir: File

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
        settingsFile.appendText("\n")

        buildFile = File(testProjectDir, "build.gradle.kts")
        buildFile.appendText("""
            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.9.10"
                id("com.github.reline.jisho")
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
    fun downloadJishoSourcesTaskSmokeTest() {
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("downloadJishoSources")
            .withPluginClasspath()
            .build()
        assertEquals(SUCCESS, result.task(":downloadJishoSources")?.outcome)
    }

    @Test
    fun prepareJishoSourcesTaskSmokeTest() {
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("prepareJishoSources")
            .withPluginClasspath()
            .build()
        assertEquals(SUCCESS, result.task(":prepareJishoSources")?.outcome)
    }

    @Test
    fun prepopulateDatabaseTaskSmokeTest() {
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("prepopulateJishoDatabase")
            .withPluginClasspath()
            .build()
        assertEquals(SUCCESS, result.task(":prepopulateJishoDatabase")?.outcome)
    }

    @Test
    fun prepopulateDatabaseAssembleTest() {
        val expectedOutputFile = "build/generated/jisho/database/jisho.sqlite"
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("assemble", "--info")
            .withPluginClasspath()
            .build()
        assertTrue(result.output.contains("Generated $expectedOutputFile"))
        assertTrue(File(testProjectDir, expectedOutputFile).exists())
        assertEquals(SUCCESS, result.task(":assemble")?.outcome)
    }

    @Test
    fun prepopulateDatabaseBuildTest() {
        val expectedOutputFile = "build/generated/jisho/database/jisho.sqlite"
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("build", "--info")
            .withPluginClasspath()
            .build()
        assertTrue(result.output.contains("Generated $expectedOutputFile"))
        assertTrue(File(testProjectDir, expectedOutputFile).exists())
        assertEquals(SUCCESS, result.task(":build")?.outcome)
    }

    @Test
    fun customOutputDirectoryTest() {
        val destination = "intermediates/assets/jisho.sqlite"
        buildFile.appendText("""
            jisho {
                database {
                    destination.set(layout.buildDirectory.file("$destination").get())
                }
            }
        """.trimIndent())
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("assemble", "--info")
            .withPluginClasspath()
            .build()
        assertTrue(result.output.contains("Generated build/$destination"))
        assertTrue(File(testProjectDir, "build/$destination").exists())
        assertEquals(SUCCESS, result.task(":assemble")?.outcome)
    }

    @Test
    fun deleteDatabaseSanityTest() {
        GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("assemble")
            .withPluginClasspath()
            .build()
        assertTrue(File(testProjectDir, "build/generated/jisho/database/jisho.sqlite").delete())
    }
}