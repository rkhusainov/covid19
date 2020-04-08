package com.github.rkhusainov.covid19.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rkhusainov.covid19.R
import com.github.rkhusainov.covid19.data.model.ResponseItem
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    private lateinit var statistics: ResponseItem

    companion object {
        private const val BUNDLE_KEY = "BUNDLE_KEY"

        fun newInstance(statItem: ResponseItem): DetailFragment {
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY, statItem)
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            statistics = arguments!!.getParcelable(BUNDLE_KEY)!!

            text_confirmed.text = statistics.cases!!.total.toString()
            text_active.text = statistics.cases!!.active.toString()
            text_recovered.text = statistics.cases!!.recovered.toString()
            text_deaths.text = statistics.deaths!!.total.toString()
        }
    }
}
