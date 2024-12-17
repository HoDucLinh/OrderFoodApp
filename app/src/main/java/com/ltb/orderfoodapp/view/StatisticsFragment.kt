package com.ltb.orderfoodapp.view

import StaticticsDAO
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.ltb.orderfoodapp.R

class StatisticsFragment : AppCompatActivity() {
    private lateinit var barChart: BarChart
    private lateinit var spinnerFilter: Spinner
    private lateinit var statisticsDAO: StaticticsDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_fragment)
        enableEdgeToEdge()
        barChart = findViewById(R.id.barChart)
        spinnerFilter = findViewById(R.id.spinner_filter)
        statisticsDAO = StaticticsDAO(this)

//        configureBarChart()

        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            val menu = Intent(this, Menu::class.java)
            startActivity(menu)
        }


        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                when (position) {
                    0 -> loadUserStatistics() // Thống kê theo người dùng
                    1 -> loadDateStatistics() // Thống kê theo ngày
                    2 -> loadMonthStatistics() // Thống kê theo tháng
                    3 -> loadCategoryStatistics() // Thống kê theo danh mục
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadUserStatistics() {
        val data = statisticsDAO.getStatisticsByUser()
        if (data.isEmpty()) {
            Log.d("Statistics", "No data available")
        } else {
            Log.d("Statistics", "Data: $data")
        }

        val entries = data.mapIndexed { index, pair -> BarEntry(index.toFloat(), pair.second) }
        val dataSet = BarDataSet(entries, "Người dùng")
        val barData = BarData(dataSet)

        barChart.data = barData

        barChart.xAxis.apply {
            isEnabled = true
            valueFormatter = IndexAxisValueFormatter(data.map { it.first })
            position = XAxis.XAxisPosition.BOTTOM_INSIDE
            setLabelCount(data.size, true)
            setGranularity(1f)
            setDrawLabels(true)
            setAvoidFirstLastClipping(true)
        }



        barChart.axisLeft.apply {
            isEnabled = true
            setGranularity(1f)
        }
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false

        barChart.invalidate()
    }



    private fun loadDateStatistics() {
        val data = statisticsDAO.getStatisticsByDate()
        if (data.isEmpty()) {
            Log.d("Statistics", "No data available")
        } else {
            Log.d("Statistics", "Data: $data")
        }
        val entries = data.mapIndexed { index, pair -> BarEntry(index.toFloat(), pair.second) }
        val dataSet = BarDataSet(entries, "Theo Ngày")
        val barData = BarData(dataSet)

        barChart.data = barData

        // Cập nhật xAxis valueFormatter
        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(data.map { it.first })
            setLabelCount(data.size, true)
            granularity = 1f
        }

        barChart.invalidate() // Cập nhật biểu đồ
    }


    private fun loadMonthStatistics() {
        val data = statisticsDAO.getStatisticsByMonth()

        if (data.isEmpty()) {
            Log.d("Statistics", "No data available")
        } else {
            Log.d("Statistics", "Data: $data")
        }

        val entries = data.mapIndexed { index, pair -> BarEntry(index.toFloat(), pair.second) }
        val dataSet = BarDataSet(entries, "Theo Tháng")
        val barData = BarData(dataSet)

        barChart.data = barData

        // Cập nhật xAxis valueFormatter
        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(data.map { it.first })
            setLabelCount(data.size, true)
            granularity = 1f
        }

        barChart.invalidate() // Cập nhật biểu đồ
    }

    private fun loadCategoryStatistics() {
        val data = statisticsDAO.getStatisticsByCategory()
        if (data.isEmpty()) {
            Log.d("Statistics", "No data available")
        } else {
            Log.d("Statistics", "Data: $data")
        }
        val entries = data.mapIndexed { index, pair -> BarEntry(index.toFloat(), pair.second) }
        val dataSet = BarDataSet(entries, "Theo Danh Mục")
        val barData = BarData(dataSet)

        barChart.data = barData

        // Cập nhật xAxis valueFormatter
        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(data.map { it.first })
            setLabelCount(data.size, true)
            granularity = 1f
        }

        barChart.invalidate() // Cập nhật biểu đồ
    }


}
