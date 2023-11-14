package com.github.reline.jisho.jmdict

import okhttp3.Interceptor
import okhttp3.Response

private const val AUTHORIZATION = "Authorization"
private const val BEARER = "Bearer"

// todo: consider renaming to "Bearer" authenticator or similar
class GithubAuthenticator(private val githubToken: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        if (githubToken != null) {
            request.header(AUTHORIZATION, "$BEARER $githubToken")
        }
        return chain.proceed(request.build())
    }
}
