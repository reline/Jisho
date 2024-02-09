package com.github.reline.jisho.compression

import com.github.reline.jisho.EUC_JP
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import org.gradle.internal.impldep.org.testng.annotations.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MonashZipTest {

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
    fun testEucjp() {
        // todo: add files
        decompressor.extractZip("test.zip".toPath(), fakeFileSystem.workingDirectory, EUC_JP)
        val expected = FileSystem.RESOURCES.read("testzip".toPath()) { readUtf8() }
        val actual = fakeFileSystem.read("testzip".toPath()) { readUtf8() }
        assertEquals(expected, actual)
    }

    @Test
    fun testDefaultUtf8() {
        // todo: add files
        decompressor.extractZip("test.zip".toPath(), fakeFileSystem.workingDirectory)
        val expected = FileSystem.RESOURCES.read("testzip".toPath()) { readUtf8() }
        val actual = fakeFileSystem.read("testzip".toPath()) { readUtf8() }
        assertEquals(expected, actual)
    }
}