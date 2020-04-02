package com.github.rkhusainov.covid19.ui.statistics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rkhusainov.covid19.R
import com.github.rkhusainov.covid19.data.model.ResponseItem
import com.github.rkhusainov.covid19.ui.contract.CountryClickListener
import kotlinx.android.synthetic.main.fragment_statistics.*

class StatisticsFragment : Fragment() {

    private lateinit var viewModelFactory: CovidViewModelFactory
    private lateinit var viewModel: StatisticsViewModel
    private lateinit var statisticsAdapter: StatisticsAdapter
    private lateinit var countryClickListener: CountryClickListener

    companion object {
        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CountryClickListener) {
            countryClickListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMVVM()
        statistics_recycler.layoutManager = LinearLayoutManager(context)
        statisticsAdapter = StatisticsAdapter(countryClickListener)
        statistics_recycler.adapter = statisticsAdapter

    }

    override fun onStart() {
        super.onStart()
        viewModel.getStatistics()
    }

    private fun setupMVVM() {
        viewModelFactory = CovidViewModelFactory()

        viewModel = ViewModelProvider(this, viewModelFactory).get(
            StatisticsViewModel::class.java
        )
        viewModel.statisticsLiveData.observe(viewLifecycleOwner,
            Observer<List<ResponseItem>> { statisticsList ->
                statisticsAdapter.bindData(
                    statisticsList ?: emptyList()
                )
            })
    }
}
