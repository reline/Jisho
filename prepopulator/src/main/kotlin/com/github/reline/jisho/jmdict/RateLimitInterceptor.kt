package com.github.reline.jisho.jmdict

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

internal const val RetryAfter = "retry-after"
internal const val RateLimitRemaining = "x-ratelimit-remaining"
internal const val RateLimitReset = "x-ratelimit-reset"

private val Response.rateLimitRemaining get() = header(RateLimitRemaining)?.toLongOrNull()
private val Response.rateLimitReset get() = header(RateLimitReset)?.toLongOrNull()?.seconds
private val Response.retryAfter get() = header(RetryAfter)?.toLongOrNull()?.seconds

class RateLimitInterceptor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) = runBlocking(ioDispatcher) {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 403 || response.code == 429) {
            val retryAfter = response.retryAfter
            val rateLimitReset = response.rateLimitReset
            val duration = when {
                retryAfter != null -> retryAfter
                response.rateLimitRemaining == 0L && rateLimitReset != null -> {
                    rateLimitReset - response.sentRequestAtMillis.milliseconds
                }
                else -> 1.minutes
            }
            delay(duration)
            chain.proceed(request)
        } else {
            response
        }
    }
}