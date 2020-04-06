package com.github.rkhusainov.covid19.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.rkhusainov.covid19.data.repository.CovidRepository
import com.github.rkhusainov.covid19.data.repository.ICovidRepository
import com.github.rkhusainov.covid19.ui.history.HistoryViewModel

class CovidViewModelFactory(private val repository: ICovidRepository = CovidRepository()) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}