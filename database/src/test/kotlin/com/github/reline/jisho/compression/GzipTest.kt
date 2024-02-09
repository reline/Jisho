package com.github.reline.jisho.compression

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GzipTest {

    private lateinit var fakeFileSystem: FakeFileSystem
    private lateinit var decompressor: DictionaryFileDecompressor

    @BeforeTest
    fun setup() {
        fakeFileSystem = FakeFileSystem()
        decompressor = DictionaryFileDecompressor(fileSystem = fakeFileSystem)
    }

    @AfterTest
    fun tearDown() {
        fakeFileSystem.checkNoOpenFiles()
    }

    @Test
    fun testXml() {
        val gzip = "test.xml.gz".toPath()
        decompressor.extractGzip(gzip, fakeFileSystem.workingDirectory)

        val xml = "test.xml".toPath()
        val expected = FileSystem.RESOURCES.read(xml) { readUtf8() }
        val actual = fakeFileSystem.read(xml) { readUtf8() }
        assertEquals(expected, actual)
    }

    @Test
    fun testPlainText() {
        // todo: add files
        decompressor.extractGzip("test.gz".toPath(), fakeFileSystem.workingDirectory)
        val expected = FileSystem.RESOURCES.read("test".toPath()) { readUtf8() }
        val actual = fakeFileSystem.read("test".toPath()) { readUtf8() }
        assertEquals(expected, actual)
    }
}
