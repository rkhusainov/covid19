package com.github.rkhusainov.covid19.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rkhusainov.covid19.R

class MainActivity : AppCompatActivity() {

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
}