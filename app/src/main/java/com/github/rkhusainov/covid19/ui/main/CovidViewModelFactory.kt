package com.github.rkhusainov.covid19.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.rkhusainov.covid19.data.repository.CovidRepository
import com.github.rkhusainov.covid19.data.repository.ICovidRepository

class CovidViewModelFactory(private val repository: ICovidRepository = CovidRepository()) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}