package com.github.reline.jisho.compression

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import org.gradle.internal.impldep.org.testng.annotations.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DictionaryFileDecompressorTest {

    private val resources = FileSystem.RESOURCES
    private lateinit var fakeFileSystem: FakeFileSystem
    private lateinit var decompressor: DictionaryFileDecompressor

    @BeforeTest
    fun setup() {
        fakeFileSystem = FakeFileSystem()
        decompressor = DictionaryFileDecompressor(system = fakeFileSystem, resources = resources)
    }

    @AfterTest
    fun tearDown() {
        fakeFileSystem.checkNoOpenFiles()
    }

    @Test
    fun testZip() {
        val fakeZip = "kradzip.zip".toPath()
        resources.read(fakeZip) {
            fakeFileSystem.write(fakeFileSystem.workingDirectory/fakeZip) {
                readAll(this)
            }
        }

        val dest = fakeFileSystem.workingDirectory/"dest".toPath()
        decompressor.extractAll(fakeFileSystem.workingDirectory, dest)
        val extractedFile = fakeZip.toFile().nameWithoutExtension.toPath()
        val expected = resources.read(extractedFile) { readUtf8() }
        val actual = fakeFileSystem.read(dest/extractedFile) { readUtf8() }
        assertEquals(expected, actual)
    }

    @Test
    fun testGZip() {
        val fakeGZip = "test.xml.gz".toPath()
        resources.read(fakeGZip) {
            fakeFileSystem.write(fakeFileSystem.workingDirectory/fakeGZip) {
                readAll(this)
            }
        }

        val dest = fakeFileSystem.workingDirectory/"dest".toPath()
        decompressor.extractAll(fakeFileSystem.workingDirectory, dest)
        val extractedFile = fakeGZip.toFile().nameWithoutExtension.toPath()
        val expected = resources.read(extractedFile) { readUtf8() }
        val actual = fakeFileSystem.read(dest/extractedFile) { readUtf8() }
        assertEquals(expected, actual)
    }
}
