package com.github.rkhusainov.covid19.data.repository

import com.github.rkhusainov.covid19.data.model.StatisticsResponse
import com.github.rkhusainov.covid19.utils.ApiUtils

class CovidRepository : ICovidRepository {
    override suspend fun getStatistics(): StatisticsResponse {
        return ApiUtils.getApi().getStatistics()
    }

    override suspend fun getHistory(country: String): StatisticsResponse {
        return ApiUtils.getApi().getHistory(country)
    }
}