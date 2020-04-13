package com.github.rkhusainov.covid19.ui.detail

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.rgb
import com.github.mikephil.charting.utils.MPPointF
import com.github.rkhusainov.covid19.R
import com.github.rkhusainov.covid19.data.model.ResponseItem
import kotlinx.android.synthetic.main.fragment_pie_chart.*


class PieChartFragment : Fragment() {

    private lateinit var statistics: ResponseItem
    private lateinit var chart: PieChart
    private val chartColors =
        mutableListOf(
            rgb("#1976D2"),
            rgb("#388E3C"),
            rgb("#FBC02D")
        )

    companion object {
        private const val BUNDLE_KEY = "BUNDLE_KEY"

        fun newInstance(statItem: ResponseItem): PieChartFragment {
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY, statItem)
            val fragment = PieChartFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_pie_chart, container, false)
        chart = view.findViewById(R.id.pie_chart)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            statistics = arguments!!.getParcelable(BUNDLE_KEY)!!
            country_text.text = statistics.country
        }
        setupPieChart()
    }

    private fun setupPieChart() {
        chart.setUsePercentValues(false)                             // значения в процентах?
        chart.description.isEnabled = false                          // показать описание?
        chart.setExtraOffsets(5f, 0f, 5f, 5f)  // положение диаграммы

        chart.dragDecelerationFrictionCoef = 0.95f                   // кожффициент трения анимации

        chart.setDrawCenterText(true)                                // показать текст по середине?
        chart.centerText = generateCenterSpannableText()             // установить текст по середине
        chart.setCenterTextTypeface(Typeface.SANS_SERIF)             // шрифт текста по середине

        chart.isDrawHoleEnabled = true           // нарисовать отверстие по середине?
        chart.setHoleColor(Color.WHITE)          // цвет отверстия по середине
        chart.holeRadius = 48f                   // радиус отверстия в середине диаграммы
        chart.transparentCircleRadius = 61f      // радиус прозрачного круга

        chart.rotationAngle = 0f                 // угол поворота диаграммы в градусах
        chart.isRotationEnabled = true           // разрешить поворот диаграммы при касании?
        chart.isHighlightPerTapEnabled = true    // разрешить выделение секции при касании?

        chart.animateY(700)         // анимация отрисовки

        // настройка легенды (ярлыков значений)
        val l: Legend = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // настройка текста ярлыков
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTypeface(Typeface.DEFAULT)
        chart.setEntryLabelTextSize(14f)

        // установка данных
        val dataSet = PieDataSet(pieData(), getString(R.string.covid19_chart))
        //dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList() // предустановленные цвета
        dataSet.colors = chartColors
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 2f                            // расстояние между секциями
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 20f                       // расстояние выделения секции

        val data = PieData(dataSet)
        //data.setValueFormatter(PercentFormatter(chart))  // формат значений в процентах
        data.setValueTextSize(12f)                         // размер текста значений
        data.setValueTextColor(Color.BLACK)                // цвет текста значений
        data.setValueTypeface(Typeface.DEFAULT)            // шрифт текста значений
        chart.data = data

        // перерисовать
        chart.invalidate()
    }

    private fun pieData(): List<PieEntry> {
        val entries = arrayListOf<PieEntry>()
        entries.add(PieEntry(statistics.cases!!.active.toFloat(), getString(R.string.active)))
        entries.add(PieEntry(statistics.cases!!.recovered.toFloat(), getString(R.string.recovered)))
        entries.add(PieEntry(statistics.deaths!!.total.toFloat(), getString(R.string.deaths)))
        return entries
    }

    /**
     * Метод для spannable текста в середине диаграммы
     */
    private fun generateCenterSpannableText(): SpannableString? {
        val s = SpannableString(getString(R.string.covid19_chart_spannable))
        s.setSpan(RelativeSizeSpan(1.7f), 0, 13, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 13, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 13, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.8f), 13, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 15, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 15, s.length, 0)
        return s
    }
}
