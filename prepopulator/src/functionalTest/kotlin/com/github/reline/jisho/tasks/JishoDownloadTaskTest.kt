package com.github.reline.jisho.tasks

import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class JishoDownloadTaskTest {
    // todo: https://github.com/junit-team/junit5/issues/2811
    // @TempDir
    private lateinit var testProjectDir: File
    private val testBuildDir get() = File(testProjectDir, "build")
    private val testResourcesDir get() = File(testProjectDir, "resources")

    private lateinit var settingsFile: File
    private lateinit var buildFile: File

    private val resources: FileSystem = FileSystem.RESOURCES
    private val system: FileSystem = FileSystem.SYSTEM

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
            import com.github.reline.jisho.JishoDownloadTask

            plugins {
                id("org.jetbrains.kotlin.jvm") version "1.9.10"
                id("com.github.reline.jisho.prepopulator") apply false
            }

        """.trimIndent())

        system.createDirectories(testResourcesDir.toOkioPath())
    }

    @Test
    fun test() {
        // todo
        /**
         * tasks.register(
         *                 "functionalDownloadTest",
         *                 JishoDownloadTask::class.java,
         *
         *             ) {
         *
         *             }
         */
    }

    @AfterEach
    fun teardown() {
        Files.walk(testProjectDir.toPath()).use {
            it.filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }
}