package com.github.reline.jisho.jmdict

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val fakeReleaseAssetResponse = """
    {
        [
            {
                "text": "大人買い",
                "reading": "おとながい",
                "furigana": [
                    {
                      "ruby": "大人",
                      "rt": "おとな"
                    }, {
                      "ruby": "買",
                      "rt": "が"
                    }, {
                      "ruby": "い"
                    }
                ]
            }        
        ]
    }
""".trimIndent()

private const val fakeVersion = "2.3.0+2023-10-25"

class JmdictClientTest {
    private lateinit var mockGithubApi: GithubReleasesApi
    private lateinit var fakeFileSystem: FakeFileSystem
    private lateinit var scope: TestScope
    private lateinit var dispatcher: TestDispatcher
    private lateinit var jmdictClient: JmdictClient

    @BeforeTest
    fun setUp() {
        mockGithubApi = mockk {
            coEvery {
                getReleaseAsset(any(), any(), any())
            } returns fakeReleaseAssetResponse.toResponseBody(MimeType.OctetStream.toMediaType())
        }
        fakeFileSystem = FakeFileSystem()
        scope = TestScope()
        dispatcher = StandardTestDispatcher(scope.testScheduler)
        jmdictClient = JmdictClient(mockGithubApi, fakeFileSystem, dispatcher)
    }

    @AfterTest
    fun tearDown() {
        fakeFileSystem.checkNoOpenFiles()
    }

    @Test
    fun testDownloadSpecifiedVersion() = scope.runTest {
        val asset = GithubAsset(0, "asset.json")
        coEvery {
            mockGithubApi.getRelease(any(), any(), fakeVersion)
        } returns GithubRelease(listOf(asset))

        jmdictClient.downloadDictionaries(fakeFileSystem.workingDirectory, fakeVersion)

        val actual = fakeFileSystem.read(asset.name.toPath()) { readUtf8() }
        assertEquals(fakeReleaseAssetResponse, actual)
    }

    @Test
    fun testDownloadUnspecifiedVersion() = scope.runTest {
        val asset = GithubAsset(0, "asset.json")
        coEvery {
            mockGithubApi.getLatestRelease(any(), any())
        } returns GithubRelease(listOf(asset))

        jmdictClient.downloadDictionaries(fakeFileSystem.workingDirectory)

        val actual = fakeFileSystem.read(asset.name.toPath()) { readUtf8() }
        assertEquals(fakeReleaseAssetResponse, actual)
    }

    @Test
    fun testDownloadJsonAssetsOnly() = scope.runTest {
        val assets = listOf("asset.txt", "asset.xml")
        coEvery {
            mockGithubApi.getLatestRelease(any(), any())
        } returns GithubRelease(assets.mapIndexed { i, name -> GithubAsset(i, name) })

        jmdictClient.downloadDictionaries(fakeFileSystem.workingDirectory)

        assertTrue(fakeFileSystem.list(fakeFileSystem.workingDirectory).isEmpty())
    }

    @Test
    fun testAvoidConcurrentRequests() = scope.runTest {
        TODO()
    }

    // todo: @Test
    fun testConditionalRequests() = scope.runTest {
        // https://docs.github.com/en/rest/using-the-rest-api/best-practices-for-using-the-rest-api#use-conditional-requests-if-appropriate
    }
}
