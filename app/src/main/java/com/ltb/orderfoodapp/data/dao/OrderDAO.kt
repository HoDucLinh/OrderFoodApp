package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.currentCompositionErrors
import com.google.type.DateTime
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Order
import com.ltb.orderfoodapp.data.model.OrderDetail
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart
import com.ltb.orderfoodapp.data.model.Restaurant
import com.ltb.orderfoodapp.data.model.Status
import com.ltb.orderfoodapp.data.model.User
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrderDAO(private val context: Context) {
    val dbHelper = DatabaseHelper.getInstance(context)



    @RequiresApi(Build.VERSION_CODES.O)
    fun addOrder(totalAmount: Int, statusId: Int, userId : Int, listProduct : List<ProductCart>) {
        val currentDateTime = LocalDateTime.now()
        val formattedDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("totalAmount", totalAmount)
            put("Status", statusId)
            put("orderDate", formattedDate)
            put("User_ID", userId)
        }
        val orderId = db.insert("\"Order\"", null, values)
        println(orderId)
        listProduct.forEach { product ->
            val cursor = db.rawQuery(
                "SELECT Price, Restaurant_ID FROM Product WHERE ID = ?",
                arrayOf(product.productId.toString())
            )
            var unitPrice: Int? = null
            var RestaurantId : Int? = null
            cursor.use {
                if (it.moveToFirst()) {
                    unitPrice = it.getInt(it.getColumnIndexOrThrow("Price"))
                    RestaurantId = it.getInt(it.getColumnIndexOrThrow("Restaurant_ID"))
                }
            }

            if (unitPrice == null) {
                throw Exception("Failed to find price for Product ID: ${product.productId}")
            }



            // Thêm vào bảng "OrderDetail"
            val orderDetail = ContentValues().apply {
                put("Order_ID", orderId)
                put("Product_ID", product.productId)
                put("Quantity", product.quantity)
                put("UnitPrice", unitPrice)
                put("Restaurant_ID",RestaurantId)
            }
            db.insert("OrderDetail", null, orderDetail)
        }
    }



    fun getOrdersByFilters(name: String?, date: String?, categoryId: Int?): List<Order> {
        val orders = mutableListOf<Order>()
        val db = dbHelper.readableDatabase
        val query = StringBuilder(
            """
        SELECT o.ID AS OrderID, o.totalAmount, o.orderStatus, o.orderDate, 
               u.ID AS UserID, u.Name AS UserName, 
               c.ID AS CategoryID, c.Name AS CategoryName,
               p.ID AS ProductID, p.Name AS ProductName, p.Price, p.Rating, p.Category_ID
        FROM Order o
        LEFT JOIN User u ON o.User_ID = u.ID
        LEFT JOIN OrderDetail od ON o.ID = od.Order_ID
        LEFT JOIN Product p ON od.Product_ID = p.ID
        LEFT JOIN Category c ON p.Category_ID = c.ID
        WHERE 1 = 1
        """
        )
        val args = mutableListOf<String>()
        if (!name.isNullOrEmpty()) {
            query.append(" AND u.Name LIKE ?")
            args.add("%$name%")
        }
        if (!date.isNullOrEmpty()) {
            query.append(" AND o.orderDate LIKE ?")
            args.add("%$date%")
        }
        if (categoryId != null) {
            query.append(" AND c.ID = ?")
            args.add(categoryId.toString())
        }

        val cursor = db.rawQuery(query.toString(), args.toTypedArray())
        cursor.use {
            while (it.moveToNext()) {
                val order = Order(
                    idOrder = it.getInt(it.getColumnIndexOrThrow("OrderID")),
                    totalAmount = it.getFloat(it.getColumnIndexOrThrow("totalAmount")),
                    orderStatus = it.getString(it.getColumnIndexOrThrow("orderStatus")),
                    orderDate = SimpleDateFormat("yyyy-MM-dd").parse(it.getString(it.getColumnIndexOrThrow("orderDate"))),
                    userId = it.getInt(it.getColumnIndexOrThrow("UserID")),
                    restaurantId = 0,
                    orderDetails = mutableListOf()
                )

                val orderDetail = OrderDetail(
                    idOrderDetail = 0, // No specific ID for order detail in query
                    orderId = order.getIdOrder(),
                    productId = it.getInt(it.getColumnIndexOrThrow("ProductID")),
                    quantity = 1, // Default value (update if required)
                    unitPrice = it.getFloat(it.getColumnIndexOrThrow("Price")),
                    totalPrice = it.getFloat(it.getColumnIndexOrThrow("Price")) // Default to unitPrice (update if required)
                )

                orderDetail.setProductId(it.getInt(it.getColumnIndexOrThrow("ProductID")))
                val categoryName = it.getString(it.getColumnIndexOrThrow("CategoryName"))
                val rating = it.getFloat(it.getColumnIndexOrThrow("Rating"))

                order.getOrderDetails().add(orderDetail)
                orders.add(order)
            }
        }
        return orders
    }

}
