package com.ltb.orderfoodapp.data.dao

//import com.ltb.orderfoodapp.data.model.Restaurant
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.produceState
import androidx.core.database.getStringOrNull
import com.ltb.orderfoodapp.adapter.GroupOrderAdapter
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.api.zalopay.helper.Helpers
import com.ltb.orderfoodapp.data.model.Order
import com.ltb.orderfoodapp.data.model.OrderDetail
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart
import com.ltb.orderfoodapp.data.model.Status
import com.ltb.orderfoodapp.data.model.User
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


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
                "SELECT Price FROM Product WHERE ID = ?",
                arrayOf(product.getProductId().toString())
            )
            var unitPrice: Int? = null
//            var RestaurantId : Int? = null
            cursor.use {
                if (it.moveToFirst()) {
                    unitPrice = it.getInt(it.getColumnIndexOrThrow("Price"))
//                    RestaurantId = it.getInt(it.getColumnIndexOrThrow("Restaurant_ID"))
                }
            }

            if (unitPrice == null) {
                throw Exception("Failed to find price for Product ID: ${product.getProductId()}")
            }



            // Thêm vào bảng "OrderDetail"
            val orderDetail = ContentValues().apply {
                put("Order_ID", orderId)
                put("Product_ID", product.getProductId())
                put("Quantity", product.getQuantity())
                put("UnitPrice", unitPrice)
//                put("Restaurant_ID",RestaurantId)
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
                    Status = it.getInt(it.getColumnIndexOrThrow("Status")),
                    orderDate = SimpleDateFormat("yyyy-MM-dd").parse(it.getString(it.getColumnIndexOrThrow("orderDate"))),
                    userId = it.getInt(it.getColumnIndexOrThrow("UserID")),
//                    restaurantId = 0,
                    orderDetails = mutableListOf()
                )

                val orderDetail = OrderDetail(
                    idOrderDetail = 0, // No specific ID for order detail in query
                    orderId = order.getIdOrder(),
                    productId = it.getInt(it.getColumnIndexOrThrow("ProductID")),
                    quantity = 1, // Default value (update if required)
                    unitPrice = it.getFloat(it.getColumnIndexOrThrow("Price"))
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
    fun getAllOrders(): MutableList<Order> {
        val orders = mutableListOf<Order>()
        val db = dbHelper.readableDatabase
        val query = """
        SELECT o.ID AS OrderID, o.totalAmount, o.Status, o.orderDate
        FROM "Order" o
        """
        val cursor = db.rawQuery(query, null)
        cursor.use {
            while (it.moveToNext()) {
                val order = Order(
                    idOrder = it.getInt(it.getColumnIndexOrThrow("OrderID")),
                    totalAmount = it.getFloat(it.getColumnIndexOrThrow("totalAmount")),
                    Status = it.getInt(it.getColumnIndexOrThrow("Status")),
                    orderDate = SimpleDateFormat("yyyy-MM-dd").parse(it.getString(it.getColumnIndexOrThrow("orderDate"))),
                )
                orders.add(order)
            }
        }
        return orders
    }

    fun getTotalRevenue(): Float {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT SUM(o.totalAmount) AS totalRevenue
        FROM "Order" o
        WHERE o.Status = 'Completed'
    """
        val cursor = db.rawQuery(query, null)
        var totalRevenue = 0f
        cursor.use {
            if (it.moveToFirst()) {
                totalRevenue = it.getFloat(it.getColumnIndexOrThrow("totalRevenue"))
            }
        }
        return totalRevenue
    }


    fun getAllProducts(statusList: List<Int>,userId: Int): List<Product> {
        val productList = mutableListOf<Product>()
        val db = dbHelper.readableDatabase

        // Create a string for the IN clause using placeholders for each status
        val statusPlaceholders = statusList.joinToString(",") { "?" }

        val query = """
        SELECT
            p.ID AS ProductID,
            p.Name AS ProductName,
            p.Price,
            p.Description,
            i.Value AS ImageSource, 
            u.ID
        FROM "Order"
        JOIN OrderDetail od ON `Order`.ID = od.Order_ID
        JOIN Product p ON od.Product_ID = p.ID
        LEFT JOIN Image i ON p.ID = i.Product_ID
        LEFT JOIN User u ON u.ID = `Order`.User_ID
        WHERE `Order`.Status IN($statusPlaceholders) AND `Order`.User_ID = ?
    """

        val cursor = db.rawQuery(query, arrayOf(statusList.map { it.toString() }.toTypedArray(),userId.toString()))

        cursor.use { cur ->
            if (cur.moveToFirst()) {
                do {
                    try {
                        val product = Product(
                            idProduct = cur.getInt(cur.getColumnIndexOrThrow("ProductID")),
                            name = cur.getString(cur.getColumnIndexOrThrow("ProductName")),
                            price = cur.getInt(cur.getColumnIndexOrThrow("Price")),
                            description = cur.getString(cur.getColumnIndexOrThrow("Description"))
                        )

                        val imageUrl = cur.getStringOrNull(cur.getColumnIndexOrThrow("ImageSource"))
                        if (!imageUrl.isNullOrEmpty()) {
                            val currentImages = product.getImages()
                            currentImages.add(imageUrl)
                            product.setImages(currentImages)
                        }
                        productList.add(product)
                    } catch (e: Exception) {
                        Log.e("getAllProducts", "Lỗi khi xử lý dòng dữ liệu: ${e.message}")
                    }
                } while (cur.moveToNext())
            }
        }
        db.close()
        return productList
    }

    fun getProductQuantityByProductId(productId: Int): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT Quantity FROM OrderDetail WHERE Product_ID = ?",
            arrayOf(productId.toString())
        )

        var quantity = 0
        cursor.use {
            if (it.moveToFirst()) {
                quantity = it.getInt(it.getColumnIndexOrThrow("Quantity"))
            }
        }
        db.close()
        return quantity
    }

    // In OrderDAO class
    fun addProductToCart(productId: Int, cartId: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("Product_ID", productId)
            put("Cart_ID", cartId)
            put("quantity", 1)  // Quantity mặc định là 1
        }

        val newRowId = db.insert("Product_Cart", null, values)
        db.close()

        if (newRowId != -1L) {
            Log.d("OrderDAO", "Thêm sản phẩm vào giỏ hàng thành công: Row ID $newRowId")
        } else {
            Log.e("OrderDAO", "Lỗi khi thêm sản phẩm vào giỏ hàng")
        }
    }

    fun getAllOrdersWithProducts(): List<GroupOrderAdapter.OrderWithProducts> {
        val db = dbHelper.readableDatabase
        val orders = mutableListOf<GroupOrderAdapter.OrderWithProducts>()

        val query = """
    SELECT 
        Product.ID AS product_id,
        Product.Name AS product_name,
        `Order`.Status,
        `Order`.User_ID,
        `Order`.ID AS OrderID,
        User.Email,
        Image.Value
    FROM 
        `Order`
    INNER JOIN 
        OrderDetail ON `Order`.ID = OrderDetail.Order_ID
    INNER JOIN 
        Product ON OrderDetail.Product_ID = Product.ID
    INNER JOIN 
        User ON User.ID == `Order`.User_ID
    LEFT JOIN  Image ON Image.Product_ID == Product.ID
""".trimIndent()

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            var currentOrderId = -1
            var currentProducts = mutableListOf<GroupOrderAdapter.ProductWithStatus>()
            var currentUserName = ""

            do {
                val orderId = cursor.getInt(cursor.getColumnIndexOrThrow("OrderID"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("Email"))

                if (currentOrderId == -1 || currentOrderId != orderId) {
                    if (currentOrderId != -1) {
                        orders.add(GroupOrderAdapter.OrderWithProducts(currentOrderId, currentProducts, currentUserName))
                    }
                    currentOrderId = orderId
                    currentProducts = mutableListOf() //Khởi tạo lại list cho order mới
                    currentUserName = email
                }


                val productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"))
                val productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"))
                val product = Product(idProduct = productId, name = productName)

                val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("Value"))
                if (imageUrl != null) {
                    val currentImages = product.getImages()
                    currentImages.add(imageUrl)
                    product.setImages(currentImages)
                }

                val status = Status.entries[cursor.getInt(cursor.getColumnIndexOrThrow("Status"))]

                currentProducts.add(GroupOrderAdapter.ProductWithStatus(product, status))

            } while (cursor.moveToNext())

            // Thêm đơn hàng cuối cùng vào danh sách
            if (currentOrderId != -1) {
                orders.add(GroupOrderAdapter.OrderWithProducts(currentOrderId, currentProducts, currentUserName))
            }
        }
        cursor.close()
        return orders
    }

    fun updateOrderStatus(orderId: Int, newStatus: Int) {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("Status", newStatus)
        }

        db.update(
            "\"Order\"",
            contentValues,
            "ID = ?",
            arrayOf(orderId.toString())
        )
    }
    fun getTopSellingProducts(): List<Product> {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT 
            p.ID AS product_id,
            p.Name AS product_name,
            SUM(od.Quantity) AS total_quantity,
            p.Price ,
            p.Rating,
            i.Value AS image_url
        FROM 
            OrderDetail od
        JOIN 
            Product p ON od.Product_ID = p.ID
        LEFT JOIN 
            Image i ON p.ID = i.Product_ID
        GROUP BY 
            p.ID
        ORDER BY 
            total_quantity DESC
        LIMIT 5
    """
        val cursor = db.rawQuery(query, null)
        val topSellingProducts = mutableListOf<Product>()

        if (cursor.moveToFirst()) {
            do {
                val productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"))
                val productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"))
                val imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
                val price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"))
                val rating = cursor.getFloat(cursor.getColumnIndexOrThrow("Rating"))

                val product = Product(idProduct = productId, name = productName,price = price, rating= rating)
                if (imageUrl != null) {
                    val currentImages = product.getImages()
                    currentImages.add(imageUrl)
                    product.setImages(currentImages)
                }

                topSellingProducts.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return topSellingProducts
    }




}
