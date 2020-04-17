package com.github.rkhusainov.covid19.ui.statistics

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    private lateinit var sortingSpinner: Spinner
    private var spinnerPosition = 0

    companion object {
        private const val SPINNER_POSITION = "SPINNER_POSITION"
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
        val view: View = inflater.inflate(R.layout.fragment_statistics, container, false)
        sortingSpinner = view.findViewById(R.id.sorting_spinner)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            spinnerPosition = savedInstanceState.getInt(SPINNER_POSITION)
            sortingSpinner.setSelection(savedInstanceState.getInt(SPINNER_POSITION))
        }

        swipeRefreshLayout.setOnRefreshListener(this)

        setupMVVM()
        statistics_recycler.layoutManager = LinearLayoutManager(context)
        statisticsAdapter = StatisticsAdapter(countryClickListener)
        statistics_recycler.adapter = statisticsAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filteredStatistics: ArrayList<ResponseItem> = arrayListOf()
                for (i in statistics.indices) {
                    if (statistics[i].country!!.toLowerCase(Locale.ROOT)
                            .contains(query.toString().toLowerCase(Locale.ROOT))
                    ) {
                        filteredStatistics.add(statistics[i])
                    }
                }
                statistics = filteredStatistics
                statisticsAdapter.bindData(statistics)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                return false
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SPINNER_POSITION, sortingSpinner.selectedItemPosition)
    }

    private fun setupMVVM() {
        viewModelFactory = CovidViewModelFactory()

        viewModel = ViewModelProvider(this, viewModelFactory).get(
            StatisticsViewModel::class.java
        )
        viewModel.statisticsLiveData.observe(viewLifecycleOwner,
            Observer<List<ResponseItem>> { statisticsList ->
                statistics = statisticsList
                initSpinner()
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

    private fun initSpinner() {
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            context!!,
            R.array.sort_items,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortingSpinner.adapter = adapter
        sortingSpinner.setSelection(spinnerPosition)

        sorting_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> statistics = statistics.sortedBy { it.cases!!.total }.reversed()
                    1 -> statistics = statistics.sortedBy { it.cases!!.active }.reversed()
                    2 -> statistics = statistics.sortedBy { it.cases!!.recovered }.reversed()
                    3 -> statistics = statistics.sortedBy { it.deaths!!.total }.reversed()
                }
                statisticsAdapter.bindData(statistics)
                spinnerPosition = sortingSpinner.selectedItemPosition
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onRefresh() {
        updateList()
    }

    private fun updateList() {
        viewModel.getStatistics()
    }
}
