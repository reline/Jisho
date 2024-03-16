package com.github.reline.jisho.jmdict

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.executeAsync
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class RateLimitInterceptorTest {
    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient
    private lateinit var scope: TestScope

    @BeforeTest
    fun setUp() {
        scope = TestScope()
        server = MockWebServer()
        server.start()
        client = OkHttpClient.Builder()
            .addInterceptor(RateLimitInterceptor(StandardTestDispatcher(scope.testScheduler)))
            .build()
    }

    @AfterTest
    fun tearDown() {
        server.shutdown()
    }

    private fun request() = Request.Builder()
        .url(server.url("/"))
        .build()

    @Test
    fun testRateLimitNotExceeded() = scope.runTest {
        server.enqueue(MockResponse().setResponseCode(Random.nextInt(200..299)))
        server.enqueue(MockResponse().setResponseCode(Random.nextInt(500..599)))
        val response = client.newCall(request()).executeAsync()
        assertTrue(response.isSuccessful)
    }

   @Test
    fun testForbidden() = scope.runTest {
        server.enqueue(MockResponse().setResponseCode(403))
        server.enqueue(MockResponse().setResponseCode(200))
        val response = client.newCall(request()).executeAsync()
        assertTrue(response.isSuccessful)
    }

    @Test
    fun testTooManyRequests() = scope.runTest {
        server.enqueue(MockResponse().setResponseCode(429))
        server.enqueue(MockResponse().setResponseCode(200))
        val response = client.newCall(request()).executeAsync()
        assertTrue(response.isSuccessful)
    }

    @Test
    fun testRetryAfter() = scope.runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(429)
                .setHeader(RetryAfter, 5.minutes.inWholeSeconds)
        )
        server.enqueue(MockResponse().setResponseCode(200))
        val response = client.newCall(request()).executeAsync()
        assertTrue(response.isSuccessful)
        assertEquals(5.minutes, currentTime.milliseconds)
    }

    @Test
    fun testRateLimitRemaining() = scope.runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setHeader(RateLimitRemaining, 1)
        )
        server.enqueue(MockResponse().setResponseCode(200))
        val response = client.newCall(request()).executeAsync()
        assertTrue(response.isSuccessful)
        assertEquals(1.minutes, currentTime.milliseconds)
    }

    @Test
    fun testRateLimitReset() = scope.runTest {
        val now = System.currentTimeMillis().milliseconds
        server.enqueue(
            MockResponse()
                .setResponseCode(429)
                .setHeader(RateLimitRemaining, 0)
                .setHeader(RateLimitReset, (now + 5.minutes).inWholeSeconds)
        )
        server.enqueue(MockResponse().setResponseCode(200))
        val response = client.newCall(request()).executeAsync()
        assertTrue(response.isSuccessful)
        // time is hard
        assertTrue(currentTime.milliseconds > 0.milliseconds)
        assertTrue(5.minutes - currentTime.milliseconds < 1.seconds)
    }

}