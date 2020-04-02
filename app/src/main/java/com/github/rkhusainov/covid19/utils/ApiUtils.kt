package com.github.rkhusainov.covid19.utils

import com.github.rkhusainov.covid19.BuildConfig
import com.github.rkhusainov.covid19.Constants
import com.github.rkhusainov.covid19.data.api.ApiKeyInterceptor
import com.github.rkhusainov.covid19.data.api.CovidApi
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtils {
    private var retrofit: Retrofit? = null
    private var gson: Gson? = null
    private var api: CovidApi? = null
    private var okHttpClient: OkHttpClient? = null

    private fun getClient(): OkHttpClient {
        if (okHttpClient == null) {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(ApiKeyInterceptor())
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)
            }
            okHttpClient = builder.build()
        }
        return okHttpClient as OkHttpClient
    }

    private fun getRetrofit(): Retrofit {
        if (gson == null) {
            gson = Gson()
        }

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    fun getApi(): CovidApi {
        if (api == null) {
            api = getRetrofit()
                .create(CovidApi::class.java)
        }
        return api!!
    }
}