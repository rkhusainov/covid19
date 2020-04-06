package com.github.rkhusainov.covid19.ui.history

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.rkhusainov.covid19.R
import com.github.rkhusainov.covid19.data.model.ResponseItem
import com.github.rkhusainov.covid19.ui.statistics.CovidViewModelFactory


class HistoryFragment : Fragment() {

    private lateinit var viewModelFactory: CovidViewModelFactory
    private lateinit var viewModel: HistoryViewModel
    private lateinit var country: String
    private lateinit var chart: LineChart

    companion object {
        private const val COUNTRY_KEY = "COUNTRY_KEY"

        fun newInstance(country: String): HistoryFragment {
            val bundle = Bundle()
            bundle.putString(COUNTRY_KEY, country)
            val fragment = HistoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)
        chart = view.findViewById(R.id.line_chart)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        country = arguments!!.getString(COUNTRY_KEY)!!

        setupMVVM()
        viewModel.getHistory(country)
    }


    private fun setupMVVM() {
        viewModelFactory = CovidViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)
        viewModel.historyLiveData.observe(viewLifecycleOwner,
            Observer<List<ResponseItem>> { historyList ->
                setupLineChart(historyList)
            }
        )
    }

    private fun setupLineChart(countryHistory: List<ResponseItem>) {
        chart.description.isEnabled = false                          // показать описание?
        chart.setTouchEnabled(true)
        chart.dragDecelerationFrictionCoef = 0.9f

        // включить масштабирование и перемещение
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)
        chart.isHighlightPerDragEnabled = true

        // если отключено, масштабирование может быть выполнено по осям x и y
        chart.setPinchZoom(false)

        // установить альтернативный цвет фона
        chart.setBackgroundColor(Color.WHITE)

        // анимация отрисовки
        chart.animateX(1500)

        // получить легенду (возможно только после настройки данных)
        val l: Legend = chart.legend;

        // настройка легенды (ярлыков значений)
        l.form = Legend.LegendForm.LINE
        l.typeface = Typeface.DEFAULT
        l.textSize = 11f
        l.textColor = Color.BLACK
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.yOffset = 11f

        // настройка оси X
        val xAxis: XAxis = chart.xAxis
        xAxis.typeface = Typeface.DEFAULT
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 11f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        // настройка оси Y слева
        val leftAxis: YAxis = chart.axisLeft
        leftAxis.typeface = Typeface.DEFAULT
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = 500000f
        leftAxis.axisMinimum = 0f
        leftAxis.labelCount = 5
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        // настройка оси Y справа
        val rightAxis: YAxis = chart.axisRight
        rightAxis.typeface = Typeface.DEFAULT
        rightAxis.textColor = Color.RED
        rightAxis.axisMaximum = 500000f
        rightAxis.axisMinimum = 0f
        rightAxis.labelCount = 5
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawZeroLine(false)
        rightAxis.isGranularityEnabled = false

        // настройка данных
        val activeSet = LineDataSet(lineData(countryHistory), "Active")

        // настройка линий графика
        activeSet.color = ColorTemplate.getHoloBlue()
        activeSet.setCircleColor(ColorTemplate.getHoloBlue())
        activeSet.lineWidth = 2f
        activeSet.circleRadius = 3f
        activeSet.fillAlpha = 65
        activeSet.fillColor = ColorTemplate.getHoloBlue()

        // настройка выделения
        activeSet.highLightColor = Color.rgb(244, 117, 117)
        activeSet.highlightLineWidth = 1f
        activeSet.setDrawHorizontalHighlightIndicator(true)
        activeSet.setDrawVerticalHighlightIndicator(true)

        // настройка отверстия в метке
        activeSet.setDrawCircleHole(true)
        activeSet.isVisible = true
        activeSet.circleHoleColor = Color.WHITE
        activeSet.circleHoleRadius = 1f

        val data = LineData(activeSet)
        data.setValueTextColor(Color.BLACK)
        data.setValueTextSize(9f)

        // установка данных
        chart.data = data

        // перерисовать
        chart.invalidate()
    }

    private fun lineData(countryHistory: List<ResponseItem>): List<Entry> {
        val entries = arrayListOf<Entry>()
        var history = countryHistory
        val calculatedStep = 1

        // переворачиваем массив, чтобы последние данные были в конце,
        // далее убираем дубликаты по дню
        history = history.reversed().distinctBy { it.day }

        for (i in history.indices step calculatedStep) {
            entries.add(Entry(i.toFloat(), history[i].cases!!.active.toFloat()))
        }
        return entries
    }
}