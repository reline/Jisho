package com.github.reline.jisho.jmdict

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BearerAuthorizationInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient

    @BeforeTest
    fun setup() {
        server = MockWebServer()
        server.start()
        client = OkHttpClient.Builder()
            .addInterceptor(BearerAuthorizationInterceptor("test"))
            .build()
    }

    @AfterTest
    fun tearDown() {
        server.shutdown()
    }

    private fun request() = Request.Builder().url(server.url("/"))

    @Test
    fun test() {
        server.enqueue(MockResponse())
        client.newCall(request().build()).execute()
        val request = server.takeRequest()
        assertEquals("Bearer test", request.getHeader("Authorization"))
    }

}