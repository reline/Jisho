package com.github.reline.jisho.tasks

import okio.FileSystem
import okio.Path
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JishoPopulateTaskTest {

    private val fileSystem = FileSystem.SYSTEM
    private val tmpDir = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
    private lateinit var testProjectPath: Path
    private val settingsFile: Path get() = testProjectPath/"settings.gradle.kts"
    private val buildFile: Path get() = testProjectPath/"build.gradle.kts"

    @BeforeEach
    fun setup() {
        testProjectPath = tmpDir/"jishoPopulateTaskTest_${UUID.randomUUID()}"

        fileSystem.createDirectories(testProjectPath, mustCreate = true)

        fileSystem.write(settingsFile, mustCreate = true) {
            writeUtf8("""
                pluginManagement {
                    repositories {
                        gradlePluginPortal()
                        google()
                        mavenCentral()
                    }
                }
    
            """.trimIndent())
        }

        fileSystem.write(buildFile, mustCreate = true) {
            writeUtf8("""
                plugins {
                    id("org.jetbrains.kotlin.jvm") version "1.9.10"
                    id("com.github.reline.jisho.prepopulator")
                }

            """.trimIndent())
        }
    }

    @AfterEach
    fun teardown() {
        fileSystem.deleteRecursively(testProjectPath, mustExist = true)
    }

    @Test
    fun smokeTest() {
        val result = GradleRunner.create()
            .withProjectDir(testProjectPath.toFile())
            .withArguments("populateJishoDatabase", "--debug")
            .withPluginClasspath()
            .forwardOutput()
            .build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":populateJishoDatabase")?.outcome)
        assertTrue(result.output.contains("Generated .*jisho.sqlite".toRegex()), result.output)
        assertTrue(fileSystem.exists(testProjectPath/"build/generated/jisho/jisho.sqlite"))
    }
}
