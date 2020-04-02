package com.github.rkhusainov.covid19.ui.statistics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rkhusainov.covid19.R
import com.github.rkhusainov.covid19.data.model.ResponseItem
import com.github.rkhusainov.covid19.ui.contract.CountryClickListener
import com.github.rkhusainov.covid19.ui.detail.PieChartFragment

class MainActivity : AppCompatActivity(), CountryClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, StatisticsFragment.newInstance())
                .commit()
        }
    }

    override fun openHistoryFragment(statItem: ResponseItem) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, PieChartFragment.newInstance(statItem))
            .addToBackStack(null)
            .commit()
    }
}
