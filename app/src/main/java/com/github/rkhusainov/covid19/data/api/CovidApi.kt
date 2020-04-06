package com.github.rkhusainov.covid19.data.api

import com.github.rkhusainov.covid19.data.model.StatisticsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidApi {
    @GET("statistics")
    suspend fun getStatistics(): StatisticsResponse

    @GET("history")
    suspend fun getHistory(@Query("country") country: String): StatisticsResponse
}