package com.github.reline.jisho.jmdict

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.math.pow
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

internal const val RetryAfter = "retry-after"
internal const val RateLimitRemaining = "x-ratelimit-remaining"
internal const val RateLimitReset = "x-ratelimit-reset"

private val DEFAULT_RETRY_DELAY = 1.minutes
private const val MAX_ATTEMPTS = 3
private val Response.rateLimitRemaining get() = header(RateLimitRemaining)?.toLongOrNull()
private val Response.rateLimitReset get() = header(RateLimitReset)?.toLongOrNull()?.seconds
private val Response.retryAfter get() = header(RetryAfter)?.toLongOrNull()?.seconds

private val Response.hasRateLimitError get() = code == 403 || code == 429

class RateLimitInterceptor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking(ioDispatcher) {
        var currentDelay = DEFAULT_RETRY_DELAY
        var attempt = 1
        var response: Response
        do {
            response = chain.proceed(chain.request())
            if (!response.hasRateLimitError) break

            val retryAfter = response.retryAfter
            val rateLimitReset = response.rateLimitReset
            currentDelay = when {
                retryAfter != null -> retryAfter
                response.rateLimitRemaining == 0L && rateLimitReset != null -> {
                    rateLimitReset - response.sentRequestAtMillis.milliseconds
                }
                attempt > 1 -> 2.0.pow(currentDelay.inWholeSeconds.toDouble()).seconds
                else -> DEFAULT_RETRY_DELAY
            }
            delay(currentDelay)
        } while (attempt++ <= MAX_ATTEMPTS)
        return@runBlocking response
    }
}
