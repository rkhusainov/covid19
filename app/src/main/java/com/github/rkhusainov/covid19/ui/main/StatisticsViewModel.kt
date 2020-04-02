package com.github.rkhusainov.covid19.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rkhusainov.covid19.data.model.ResponseItem
import com.github.rkhusainov.covid19.data.repository.ICovidRepository
import kotlinx.coroutines.launch

class StatisticsViewModel(private val repository: ICovidRepository) : ViewModel() {
    val statisticsLiveData = MutableLiveData<List<ResponseItem>>()

    fun getStatistics() {
        viewModelScope.launch {
            val statistics = repository.getStatistics().response
            statisticsLiveData.value = statistics
        }
    }
}