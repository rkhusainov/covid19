package com.github.rkhusainov.covid19.ui.statistics

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.rkhusainov.covid19.R
import com.github.rkhusainov.covid19.data.model.ResponseItem
import com.github.rkhusainov.covid19.ui.contract.CountryClickListener
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import java.util.*

class StatisticsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModelFactory: CovidViewModelFactory
    private lateinit var viewModel: StatisticsViewModel
    private lateinit var statisticsAdapter: StatisticsAdapter
    private lateinit var countryClickListener: CountryClickListener
    private lateinit var statistics: List<ResponseItem>
    private lateinit var filteredStatistics: ArrayList<ResponseItem>

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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout.setOnRefreshListener(this)

        setupMVVM()
        statistics_recycler.layoutManager = LinearLayoutManager(context)
        statisticsAdapter = StatisticsAdapter(countryClickListener)
        statistics_recycler.adapter = statisticsAdapter

        updateList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filteredStatistics = arrayListOf()
                for (i in statistics.indices) {
                    if (statistics[i].country!!.toLowerCase(Locale.ROOT)
                            .contains(query.toString().toLowerCase(Locale.ROOT))
                    ) {
                        filteredStatistics.add(statistics[i])
                    }
                }
                statisticsAdapter.bindData(filteredStatistics)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                return false
            }
        })
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
                statistics = statisticsList
            })

        viewModel.isNetworkError.observe(viewLifecycleOwner, Observer {
            if (it) {
                error_view.visibility = View.VISIBLE
                statistics_recycler.visibility = View.GONE
            } else {
                statistics_recycler.visibility = View.VISIBLE
                error_view.visibility = View.GONE
            }
        })

        viewModel.isLoadingData.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = it
        })
    }

    private fun updateList() {
        viewModel.getStatistics()
    }

    override fun onRefresh() {
        updateList()
    }
}
