package com.github.rkhusainov.covid19.ui.contract

import com.github.rkhusainov.covid19.data.model.ResponseItem

interface CountryClickListener {
    fun openDetailFragment(statItem: ResponseItem)
}