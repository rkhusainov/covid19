package com.github.rkhusainov.covid19.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rkhusainov.covid19.data.model.ResponseItem
import com.github.rkhusainov.covid19.data.repository.ICovidRepository
import kotlinx.coroutines.launch

class StatisticsViewModel(private val repository: ICovidRepository) : ViewModel() {
    private val _isNetworkError = MutableLiveData(false)
    private val _isLoadingData = MutableLiveData(false)
    private val _statisticsLiveData = MutableLiveData<List<ResponseItem>>()

    fun getStatistics() {
        viewModelScope.launch {
            _isLoadingData.value = true
            try {
                val statistics = repository.getStatistics().response
                _statisticsLiveData.value = statistics
                _isNetworkError.value = false
            } catch (exception: Exception) {
                _isNetworkError.value = true
            } finally {
                _isLoadingData.value = false
            }
        }
    }

    val isNetworkError: LiveData<Boolean> = _isNetworkError
    val isLoadingData: LiveData<Boolean> = _isLoadingData
    val statisticsLiveData: LiveData<List<ResponseItem>> = _statisticsLiveData
}