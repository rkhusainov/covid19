package com.github.rkhusainov.covid19.ui.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rkhusainov.covid19.R
import com.github.rkhusainov.covid19.data.model.ResponseItem
import com.github.rkhusainov.covid19.ui.contract.CountryClickListener
import kotlinx.android.synthetic.main.statistics_item.view.*

class StatisticsAdapter(private val countryClickListener: CountryClickListener) :
    RecyclerView.Adapter<StatisticsAdapter.StatisticsHolder>() {

    class StatisticsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var statistics: List<ResponseItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.statistics_item, parent, false)
        return StatisticsHolder(view)
    }

    fun bindData(data: List<ResponseItem>) {
        statistics = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = statistics.size


    override fun onBindViewHolder(holder: StatisticsHolder, position: Int) {
        val statItem: ResponseItem = statistics[position]
        holder.itemView.country_text.text = statItem.country
        holder.itemView.active_text.text = statItem.cases?.active.toString()
        holder.itemView.critical_text.text = statItem.cases?.critical.toString()
        holder.itemView.recovered_text.text = statItem.cases?.recovered.toString()
        holder.itemView.new_text.text = statItem.cases?.new
        holder.itemView.deaths_text.text = statItem.deaths?.total.toString()

        holder.itemView.setOnClickListener {
            countryClickListener.openDetailFragment(statItem)
        }
    }
}