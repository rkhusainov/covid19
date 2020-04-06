package com.github.rkhusainov.covid19.data.repository

import com.github.rkhusainov.covid19.data.model.StatisticsResponse

interface ICovidRepository {
    suspend fun getStatistics(): StatisticsResponse
    suspend fun getHistory(country: String): StatisticsResponse
}