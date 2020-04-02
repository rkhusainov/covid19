package com.github.rkhusainov.covid19.data.api

import com.github.rkhusainov.covid19.data.model.StatisticsResponse
import retrofit2.http.GET

interface CovidApi {
    @GET("statistics")
    suspend fun getStatistics(): StatisticsResponse
}