package com.github.reline.jisho.tasks

import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import okio.buffer
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResourceExtractionTaskTest {
    // todo: https://github.com/junit-team/junit5/issues/2811
    // @TempDir
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
    fun gzipTest() {
        buildFile.appendText("""
            import com.github.reline.jisho.tasks.ResourceExtractionTask
            
            tasks.register("extractGzip", ResourceExtractionTask::class.java) {
                fromResource(File("edict2.gz"))
                into(project.layout.buildDirectory.get().asFile)
            }

        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("extractGzip")
            .withPluginClasspath()
            .build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":extractGzip")?.outcome)
    }

    @Test
    fun zipTest() {
        buildFile.appendText("""
            import com.github.reline.jisho.tasks.ResourceExtractionTask
            
            tasks.register("extractZip", ResourceExtractionTask::class) {
                fromResource(File("kradzip.zip"))
                into(project.layout.buildDirectory.get().asFile)
            }
            
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("extractZip")
            .withPluginClasspath()
            .build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":extractZip")?.outcome)
    }

}
