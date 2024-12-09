import android.os.Bundle
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.ltb.orderfoodapp.R

class Statistics_Fragment : AppCompatActivity() {
    private lateinit var barChart: BarChart
    private lateinit var spinnerFilter: Spinner
    private lateinit var statisticsDAO: StaticticsDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_fragment)

        barChart = findViewById(R.id.barChart)
        spinnerFilter = findViewById(R.id.spinner_filter)
        statisticsDAO = StaticticsDAO(this)

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
        val entries = data.mapIndexed { index, pair -> BarEntry(index.toFloat(), pair.second) }
        val dataSet = BarDataSet(entries, "Người dùng")
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.invalidate() // Cập nhật biểu đồ
    }

    // Tương tự tạo các hàm loadDateStatistics(), loadMonthStatistics(), loadCategoryStatistics()
    private fun loadDateStatistics() {
        val data = statisticsDAO.getStatisticsByDate()
        val entries = data.mapIndexed { index, pair -> BarEntry(index.toFloat(), pair.second) }
        val dataSet = BarDataSet(entries, "Theo Ngày")
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.invalidate() // Cập nhật biểu đồ
    }
    private fun loadMonthStatistics() {
        val data = statisticsDAO.getStatisticsByMonth()
        val entries = data.mapIndexed { index, pair -> BarEntry(index.toFloat(), pair.second) }
        val dataSet = BarDataSet(entries, "Theo Tháng")
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.invalidate() // Cập nhật biểu đồ
    }
    private fun loadCategoryStatistics() {
        val data = statisticsDAO.getStatisticsByCategory()
        val entries = data.mapIndexed { index, pair -> BarEntry(index.toFloat(), pair.second) }
        val dataSet = BarDataSet(entries, "Theo Danh Mục")
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.invalidate() // Cập nhật biểu đồ
    }


}
