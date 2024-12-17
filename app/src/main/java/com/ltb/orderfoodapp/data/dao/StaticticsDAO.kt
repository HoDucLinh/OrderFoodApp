import android.content.Context
import android.util.Log
import com.ltb.orderfoodapp.data.DatabaseHelper

class StaticticsDAO(private val context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun getStatisticsByUser(): List<Pair<String, Float>> {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT User.FullName, SUM(`Order`.totalAmount) AS TotalSpent
        FROM `Order`
        INNER JOIN User ON `Order`.User_ID = User.ID
        GROUP BY User.ID
        ORDER BY TotalSpent DESC
    """
        val cursor = db.rawQuery(query, null)
        val data = mutableListOf<Pair<String, Float>>()

        while (cursor.moveToNext()) {
            val user = cursor.getString(0)
            val totalSpent = cursor.getFloat(1)
            Log.d("Statistics", "User: $user, TotalSpent: $totalSpent") // In dữ liệu để kiểm tra
            data.add(Pair(user, totalSpent))
        }
        cursor.close()

        if (data.isEmpty()) {
            Log.d("Statistics", "No data found.")
        } else {
            Log.d("Statistics", "Data fetched: ${data.size} records.")
        }

        return data
    }

    fun getStatisticsByDate(): List<Pair<String, Float>> {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT DATE(`Order`.orderDate) AS OrderDate, SUM(`Order`.totalAmount) AS TotalSpent
        FROM `Order`
        GROUP BY OrderDate
        ORDER BY OrderDate ASC
    """
        val cursor = db.rawQuery(query, null)
        val data = mutableListOf<Pair<String, Float>>()
        while (cursor.moveToNext()) {
            val date = cursor.getString(0) // Ngày
            val totalSpent = cursor.getFloat(1) // Tổng tiền
            data.add(Pair(date, totalSpent))
        }
        cursor.close()
        return data
    }

    fun getStatisticsByMonth(): List<Pair<String, Float>> {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT strftime('%Y-%m', `Order`.orderDate) AS OrderMonth, SUM(`Order`.totalAmount) AS TotalSpent
        FROM `Order`
        GROUP BY OrderMonth
        ORDER BY OrderMonth ASC
    """
        val cursor = db.rawQuery(query, null)
        val data = mutableListOf<Pair<String, Float>>()
        while (cursor.moveToNext()) {
            val month = cursor.getString(0)
            val totalSpent = cursor.getFloat(1)
            data.add(Pair(month, totalSpent))
            println("Log: Month = $month, TotalSpent = $totalSpent")
        }

        cursor.close()
        return data
    }
    fun getStatisticsByCategory(): List<Pair<String, Float>> {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT Category.Name AS CategoryName, SUM(OrderDetail.Quantity * Product.Price) AS TotalSpent
        FROM OrderDetail
        INNER JOIN Product ON OrderDetail.Product_ID = Product.ID
        INNER JOIN Category ON Product.Category_ID = Category.ID
        GROUP BY Category.ID
        ORDER BY TotalSpent DESC
    """
        val cursor = db.rawQuery(query, null)
        val data = mutableListOf<Pair<String, Float>>()
        while (cursor.moveToNext()) {
            val category = cursor.getString(0) // Tên danh mục
            val totalSpent = cursor.getFloat(1) // Tổng tiền
            data.add(Pair(category, totalSpent))
        }
        cursor.close()
        return data
    }



}
