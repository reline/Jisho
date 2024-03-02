package com.github.reline.jisho.jmdict

import okhttp3.Interceptor
import okhttp3.Response

const val AUTHORIZATION = "Authorization"
private const val BEARER = "Bearer"

class BearerAuthorizationInterceptor(private val bearerToken: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = with(chain.request()) {
            if (bearerToken != null) {
                newBuilder().header(AUTHORIZATION, "$BEARER $bearerToken").build()
            } else this
        }
        return chain.proceed(request)
    }
}
