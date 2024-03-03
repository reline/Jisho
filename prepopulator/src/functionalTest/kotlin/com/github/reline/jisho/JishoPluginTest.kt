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
                id("com.github.reline.jisho.database")
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
    fun prepareJishoSourcesTaskVerifyFilesTest() {
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("prepareJishoSources")
            .withPluginClasspath()
            .build()
        assertEquals(SUCCESS, result.task(":prepareJishoSources")?.outcome)
        val jishoSources = listOf(
            "JMdict_e.xml",
            "JMnedict.xml",
            "JmdictFurigana.json",
            "JmnedictFurigana.json",
            "kanjidic2.xml",
            "radkfile",
            "radkfile2",
            "radkfilex",
            "kradfile",
            "kradfile2",
        ).map { File(testBuildDir, "intermediates/jisho/$it") }
        jishoSources.forEach {
            assertTrue(it.exists(), "${it.absolutePath} does not exist")
        }
    }

    @Test
    fun prepopulateDatabaseTaskSmokeTest() {
        val expectedOutputFile = "build/generated/jisho/database/jisho.sqlite"
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("prepopulateJishoDatabase")
            .withPluginClasspath()
            .build()
        assertEquals(SUCCESS, result.task(":prepopulateJishoDatabase")?.outcome)
        assertTrue(result.output.contains("Generated $expectedOutputFile"))
        assertTrue(File(testProjectDir, expectedOutputFile).exists())
    }

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
            .withArguments("prepopulateJishoDatabase", "--info")
            .withPluginClasspath()
            .build()
        assertTrue(result.output.contains("Generated build/$destination"))
        assertTrue(File(testBuildDir, destination).exists())
        assertEquals(SUCCESS, result.task(":prepopulateJishoDatabase")?.outcome)
    }

    @Test
    fun deleteDatabaseSanityTest() {
        GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("prepopulateJishoDatabase")
            .withPluginClasspath()
            .build()
        assertTrue(File(testBuildDir, "generated/jisho/database/jisho.sqlite").delete())
    }
}