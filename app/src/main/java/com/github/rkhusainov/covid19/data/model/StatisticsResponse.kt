package com.github.rkhusainov.covid19.data.model

import com.google.gson.annotations.SerializedName

data class StatisticsResponse(

    @SerializedName("get") val get: String,
    @SerializedName("parameters") val parameters: Any,
    @SerializedName("errors") val errors: List<String>,
    @SerializedName("results") val results: Int,
    @SerializedName("response") val response: List<ResponseItem>
)