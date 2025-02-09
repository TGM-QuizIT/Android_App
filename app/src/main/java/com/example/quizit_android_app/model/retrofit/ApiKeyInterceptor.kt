package com.example.quizit_android_app.model.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithApiKey = originalRequest.newBuilder()
            .header("Authorization", apiKey)
            .build()
        return chain.proceed(requestWithApiKey)
    }
}