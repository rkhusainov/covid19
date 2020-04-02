package com.github.rkhusainov.covid19.data.api

import com.github.rkhusainov.covid19.Constants
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    companion object {
        private const val PROPERTY_HOST = "x-rapidapi-host"
        private const val PROPERTY_API_KEY = "x-rapidapi-key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .header(PROPERTY_HOST, Constants.HOST)
            .header(PROPERTY_API_KEY, Constants.API_KEY)
            .build()
        return chain.proceed(request)
    }

}