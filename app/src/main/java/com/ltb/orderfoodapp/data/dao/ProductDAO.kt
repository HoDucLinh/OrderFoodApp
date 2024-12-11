package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Image
import com.ltb.orderfoodapp.data.model.Product

class ProductDAO(context: Context) {
    private val db: SQLiteDatabase
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var imageDAO: ImageDAO
//    private lateinit var restaurantDAO: RestaurantDAO

    init {
        db = DatabaseHelper.getInstance(context).writableDatabase
        categoryDAO = CategoryDAO(context)
//        restaurantDAO = RestaurantDAO(context)

    }


    fun close() {
        db.close()

    }

    fun demo() {
        val product = Product(
            name = "Pizza Margherita",
            price = 150000,
            rating = 4.5f,
            description = "Pizza với phô mai và cà chua tươi",
//            restaurant = "Pizza Hut",
            category = "Pizza",
            images = mutableListOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg"
            )
        )
        addProduct(product)
    }

    fun addProduct(product: Product): Long {
//        if (product.name.isEmpty() || product.price <= 0 || product.rating < 0 || product.description.isEmpty()) {
//            throw IllegalArgumentException("Invalid product data")
//        }

        return try {
            val productId = insertProduct(product)

            val categoryId = getOrInsertCategory(product.getCategory())
            updateProductCategory(productId, categoryId)

            val restaurantId = getOrInsertRestaurant(product.getRestaurant())
            updateProductRestaurant(productId, restaurantId)

            // Thêm hình ảnh vào sản phẩm
            if (product.getImages().isNotEmpty()) {
                addImagesToProduct(productId, product.getImages())
            }

            productId
        } catch (e: Exception) {
            throw e
        }
    }

    // Thêm sản phẩm vào cơ sở dữ liệu
    private fun insertProduct(product: Product): Long {
        val values = ContentValues().apply {
            put("Name", product.getName())
            put("Price", product.getPrice())
            put("Rating", product.getRating())
            put("Description", product.getDescription())
        }
        return db.insert("Product", null, values)
    }

    // Kiểm tra xem danh mục có tồn tại chưa, nếu chưa thì thêm mới
    private fun getOrInsertCategory(categoryName: String): Int {
        var categoryId = categoryDAO.getCategoryIdByName(categoryName)
        if (categoryId == -1) {
            val categoryValues = ContentValues().apply {
                put("Name", categoryName)
                put("Description", "dfasdf")
            }
            val newCategoryId = db.insert("Category", null, categoryValues)
            categoryId = newCategoryId.toInt()
        }
        return categoryId
    }

    // Cập nhật sản phẩm với category_id
    private fun updateProductCategory(productId: Long, categoryId: Int) {
        val updateCategoryValues = ContentValues().apply {
            put("Category_ID", categoryId)
        }
        db.update("Product", updateCategoryValues, "ID = ?", arrayOf(productId.toString()))
    }

    // Kiểm tra xem nhà hàng có tồn tại chưa, nếu chưa thì thêm mới
//    private fun getOrInsertRestaurant(restaurantName: String): Int {
//        var restaurantId = restaurantDAO.getRestaurantIdByName(restaurantName)
//        if (restaurantId == -1) {
//            val restaurantValues = ContentValues().apply {
//                put("Name", restaurantName)
//                put("Address", " ")
//            }
//            val newRestaurantId = db.insert("Restaurant", null, restaurantValues)
//            restaurantId = newRestaurantId.toInt()
//        }
//        return restaurantId
//    }

    // Cập nhật sản phẩm với restaurant_id
    private fun updateProductRestaurant(productId: Long, restaurantId: Int) {
        val updateRestaurantValues = ContentValues().apply {
            put("Restaurant_ID", restaurantId)
        }
        db.update("Product", updateRestaurantValues, "ID = ?", arrayOf(productId.toString()))
    }

    // Thêm hình ảnh cho sản phẩm
    private fun addImagesToProduct(productId: Long, images: List<String>) {
        val imageDAO = ImageDAO(db)
        images.forEach { imageUrl ->
            try {
                imageDAO.addImage(imageUrl, productId.toInt())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllProducts(): MutableList<Product> {
        val productList = mutableListOf<Product>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                """ 
            SELECT 
                p.ID, p.Name, p.Price, p.Rating, p.Description, 
                c.Name AS CategoryName, r.Name AS RestaurantName, i.Value AS ImageSource 
            FROM Product p
            LEFT JOIN Category c ON p.Category_ID = c.ID
            LEFT JOIN Image i ON p.ID = i.Product_ID
            LEFT JOIN Restaurant r ON p.Restaurant_ID = r.ID
            """, null
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val product = Product(
                        idProduct = it.getInt(it.getColumnIndexOrThrow("ID")),
                        name = it.getString(it.getColumnIndexOrThrow("Name")),
                        price = it.getInt(it.getColumnIndexOrThrow("Price")),
                        rating = it.getFloat(it.getColumnIndexOrThrow("Rating")),
                        description = it.getString(it.getColumnIndexOrThrow("Description")),
                    )
                    val restaurant = it.getString(it.getColumnIndexOrThrow("RestaurantName"))
                    product.setRestaurant(restaurant)
                    val categoryName = it.getString(it.getColumnIndexOrThrow("CategoryName"))
                    product.setCategory(categoryName)
                    val imageUrl = it.getString(it.getColumnIndexOrThrow("ImageSource"))
                    if (imageUrl != null) {
                        val currentImages = product.getImages()
                        currentImages.add(imageUrl)
                        product.setImages(currentImages)
                    }


                    productList.add(product)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close() // Đảm bảo đóng cursor khi không còn sử dụng
        }
        return productList
    }
    fun getProductById(productId: Int): Product {
        val cursor = db.rawQuery(
            "SELECT p.ID, p.Name, p.Price, p.Rating, p.Description FROM Product p WHERE p.ID = ?",
            arrayOf(productId.toString())
        )

        cursor?.use {
            if (it.moveToFirst()) {
                return Product(
                    idProduct = it.getInt(it.getColumnIndexOrThrow("ID")),
                    name = it.getString(it.getColumnIndexOrThrow("Name")),
                    price = it.getInt(it.getColumnIndexOrThrow("Price")),
                    rating = it.getFloat(it.getColumnIndexOrThrow("Rating")),
                    description = it.getString(it.getColumnIndexOrThrow("Description"))
                )
            }
        }

        throw IllegalArgumentException("Product not found")
    }

    fun deleteProduct(productId: Int): Boolean {
        val rowsDeleted = db.delete("Product", "ID = ?", arrayOf(productId.toString()))
        return rowsDeleted > 0
    }
    fun updateProduct(product: Product): Boolean {
        val values = ContentValues().apply {
            put("Name", product.getName())
            put("Price", product.getPrice())
            put("Rating", product.getRating())
            put("Description", product.getDescription())
        }

        val rowsUpdated = db.update("Product", values, "ID = ?", arrayOf(product.getIdProduct().toString()))
        return rowsUpdated > 0
    }
    // Xóa sản phẩm và ảnh liên quan
    fun deleteProductById(productId: Int): Boolean {
        return try {
            // Xóa ảnh liên quan (nếu có)
            deleteProductImages(productId)

            // Xóa sản phẩm từ bảng Product
            val rowsAffected = db.delete("Product", "ID = ?", arrayOf(productId.toString()))
            rowsAffected > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Xóa ảnh liên quan đến sản phẩm
    fun deleteProductImages(productId: Int):Boolean {
        try {
            db.delete("Image", "Product_ID = ?", arrayOf(productId.toString()))
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
    fun getAllCategories(): List<String> {
        val categories = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT Name FROM Category", null)

        cursor.use {
            while (it.moveToNext()) {
                categories.add(it.getString(0)) // Lấy cột Name
            }
        }

        return categories
    }

    // Lấy toàn bộ productID
    fun getAllProductIds(): List<Int> {
        val productIds = mutableListOf<Int>()
        val query = "SELECT ID FROM Product"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                productIds.add(id)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return productIds
    }

    // Lấy trung bình đánh giá
    fun getAverageRatingForProduct(productId: Int): Float {
        val query = """
        SELECT AVG(Rating) AS AverageRating
        FROM ReviewOrder
        WHERE Product_ID = ?
    """
        val cursor = db.rawQuery(query, arrayOf(productId.toString()))
        var averageRating = 0f
        if (cursor.moveToFirst()) {
            averageRating = cursor.getFloat(cursor.getColumnIndexOrThrow("AverageRating"))
        }
        cursor.close()
        return averageRating
    }

    // update rating lên trên database
    fun updateProductRating(productId: Int, newRating: Float) {
        val values = ContentValues().apply {
            put("Rating", newRating)
        }
        db.update("Product", values, "ID = ?", arrayOf(productId.toString()))
    }

    // Đồng bộ hoá
    fun syncProductRatings() {
        val productIds = getAllProductIds() // Hàm lấy tất cả ID sản phẩm
        for (productId in productIds) {
            val averageRating = getAverageRatingForProduct(productId)
            updateProductRating(productId, averageRating)
        }
    }



}
