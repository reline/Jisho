package com.github.reline.jisho.tasks

import okio.FileSystem
import okio.Path
import okio.buffer
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResourceExtractionTaskTest {
    private val fileSystem = FileSystem.SYSTEM
    private val tmpDir = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
    private lateinit var testProjectPath: Path
    private val settingsFile: Path get() = testProjectPath/"settings.gradle.kts"
    private val buildFile: Path get() = testProjectPath/"build.gradle.kts"

    @BeforeEach
    fun setup() {
        testProjectPath = tmpDir/"resourceExtractionTaskTest_${UUID.randomUUID()}"
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
    fun gzipTest() {
        fileSystem.appendingSink(buildFile, mustExist = true).buffer().use { sink ->
            sink.writeUtf8("""
                import com.github.reline.jisho.tasks.GzipResourceExtractionTask
                
                tasks.register("extractGzip", GzipResourceExtractionTask::class.java) {
                    resourceAssetPath.set("edict2.gz")
                    outputFile.set(project.layout.buildDirectory.file("edict2"))
                }

            """.trimIndent())
        }

        val result = GradleRunner.create()
            .withProjectDir(testProjectPath.toFile())
            .withArguments("extractGzip")
            .withPluginClasspath()
            .build()

        val file = testProjectPath/"build"/"edict2"

        assertEquals(TaskOutcome.SUCCESS, result.task(":extractGzip")?.outcome)
        assertTrue(fileSystem.exists(file), "$file does not exist")
    }

    @Disabled("Not implemented yet")
    @Test
    fun zipTest() {
        fileSystem.appendingSink(buildFile, mustExist = true).buffer().use { sink ->
            sink.writeUtf8("""
                import com.github.reline.jisho.tasks.ZipResourceExtractionTask
                
                tasks.register("extractZip", ZipResourceExtractionTask::class.java) {
                    resourceAssetPath.set("kradzip.gz")
                    outputDirectory.set(project.layout.buildDirectory)
                }

            """.trimIndent())
        }

        val result = GradleRunner.create()
            .withProjectDir(testProjectPath.toFile())
            .withArguments("extractZip", "--stacktrace")
            .withPluginClasspath()
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":extractZip")?.outcome)

        val expected = listOf(
            "radkfile2",
            "radkfilex",
            "kradfile",
            "kradfile2",
        )
        val doesNotExist = expected
            .map { testProjectPath/"build"/it }
            .filterNot { fileSystem.exists(it) }
        assertTrue(doesNotExist.isEmpty(), "Failed to find ${doesNotExist.joinToString()}")
    }

}
