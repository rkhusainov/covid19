package com.github.rkhusainov.covid19.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.rkhusainov.covid19.data.model.ResponseItem
import com.github.rkhusainov.covid19.data.repository.ICovidRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: ICovidRepository) : ViewModel() {
    val historyLiveData = MutableLiveData<List<ResponseItem>>()

    fun getHistory(country: String) {
        viewModelScope.launch {
            val history = repository.getHistory(country).response
            historyLiveData.value = history
        }
    }
}