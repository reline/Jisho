package com.github.reline.jisho.compression

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CompressionTest {

    private val resources = FileSystem.RESOURCES
    private lateinit var fakeFileSystem: FakeFileSystem

    @BeforeTest
    fun setup() {
        fakeFileSystem = FakeFileSystem()
    }

    @AfterTest
    fun tearDown() {
        fakeFileSystem.checkNoOpenFiles()
    }

    @Test
    fun testZip() {
        val fakeZip = "kradzip.zip".toPath()
        val dest = fakeFileSystem.workingDirectory/"dest".toPath()
        resources.extractZip(fakeZip, fakeFileSystem to dest)
        val extractedFile = fakeZip.toFile().nameWithoutExtension.toPath()
        val expected = resources.read(extractedFile) { readUtf8() }
        val actual = fakeFileSystem.read(dest/extractedFile) { readUtf8() }
        assertEquals(expected.trimMargin(), actual.trimMargin())
    }

    @Test
    fun testGzip() {
        val fakeGzip = "test.xml.gz".toPath()
        val extractedFile = fakeGzip.toFile().nameWithoutExtension.toPath()
        val dest = fakeFileSystem.workingDirectory/extractedFile
        resources.extractGzip(fakeGzip, fakeFileSystem to dest)
        val expected = resources.read(extractedFile) { readUtf8() }
        val actual = fakeFileSystem.read(dest) { readUtf8() }
        assertEquals(expected.trimMargin(), actual.trimMargin())
    }
}
