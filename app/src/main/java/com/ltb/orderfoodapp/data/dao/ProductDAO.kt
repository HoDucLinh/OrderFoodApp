package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Image
import com.ltb.orderfoodapp.data.model.Product

class ProductDAO(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private lateinit var db: SQLiteDatabase
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var imageDAO: ImageDAO
    private lateinit var restaurantDAO: RestaurantDAO

    init {
        open()

    }
    fun open() {
        db = dbHelper.writableDatabase
        categoryDAO = CategoryDAO(db)
        restaurantDAO = RestaurantDAO(db)
    }

    fun close() {
        dbHelper.close()

    }
    fun demo(){
        val product = Product(
            name = "Pizza Margherita",
            price = 150000,
            rating = 4.5f,
            description = "Pizza với phô mai và cà chua tươi",
            restaurant = "Pizza Hut",  // Tên nhà hàng
            category = "Pizza",        // Danh mục
            images = mutableListOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg"
            )
        )
        addProduct(product)
    }
    // Them san phanm va them category , iamge, restaurantName
    fun addProduct(product: Product): Long? {
        if (product.name.isEmpty() || product.price <= 0 || product.rating < 0 || product.description.isEmpty()) {
            return null
        }

        try {
            val values = ContentValues().apply {
                put("Name", product.name)
                put("Price", product.price)
                put("Rating", product.rating)
                put("Description", product.description)
            }
            val productId = db.insert("Product", null, values)

            var categoryId = categoryDAO.getCategoryIdByName(product.category)
            if (categoryId == -1) {
                val categoryValues = ContentValues().apply {
                    put("Name", product.category)
                    put("Description", "dfasdf")
                }
                val newCategoryId = db.insert("Category", null, categoryValues)
                categoryId = newCategoryId.toInt()
            }
            val updateCategoryValues = ContentValues().apply {
                put("Category_ID", categoryId)
            }
            db.update("Product", updateCategoryValues, "ID = ?", arrayOf(productId.toString()))

            var restaurantId = restaurantDAO.getRestaurantIdByName(product.restaurant)
            if (restaurantId == -1) {
                val restaurantValues = ContentValues().apply {
                    put("Name", product.restaurant)
                    put("Address", " ")
                }
                val newRestaurantId = db.insert("Restaurant", null, restaurantValues)
                restaurantId = newRestaurantId.toInt()
            }
            val updateRestaurantValues = ContentValues().apply {
                put("Restaurant_ID", restaurantId)
            }
            db.update("Product", updateRestaurantValues, "ID = ?", arrayOf(productId.toString()))

            val imageDAO = ImageDAO(db)
            product.images.forEach { imageUrl ->
                imageDAO.addImage(imageUrl, productId.toInt())
            }

            return productId
        } catch (e: Exception) {
            // Handle database error
            return null
        }
    }




    fun getAllProducts(): MutableList<Product> {
        val productList = mutableListOf<Product>()

        val cursor = db.rawQuery("""
        SELECT 
            p.ID, p.Name, p.Price, p.Rating, p.Description, 
            c.Name AS CategoryName, r.Name AS RestaurantName, i.Value AS ImageSource 
        FROM Product p
        LEFT JOIN Category c ON p.Category_ID = c.ID
        LEFT JOIN Image i ON p.ID = i.Product_ID
        LEFT JOIN Restaurant r ON p.Restaurant_ID = r.ID
    """, null)
        cursor.use {
            while (it.moveToNext()) {
                val product = Product(
                    idProduct = it.getInt(it.getColumnIndexOrThrow("ID")),
                    name = it.getString(it.getColumnIndexOrThrow("Name")),
                    price = it.getInt(it.getColumnIndexOrThrow("Price")),
                    rating = it.getFloat(it.getColumnIndexOrThrow("Rating")),
                    description = it.getString(it.getColumnIndexOrThrow("Description")),
                )
                val restaurant = it.getString(it.getColumnIndexOrThrow("RestaurantName"))
                product.restaurant = restaurant
                val categoryName = it.getString(it.getColumnIndexOrThrow("CategoryName"))
                product.category = categoryName
                val imageUrl = it.getString(it.getColumnIndexOrThrow("ImageSource"))
                if (imageUrl != null) {
                    product.images.add(imageUrl)
                }

                productList.add(product)
            }
        }

        return productList
    }


//
//    // Cập nhật sản phẩm
//    fun updateProduct(product: Product): Int {
//        val values = ContentValues().apply {
//            put("name", product.name)
//            put("storeName", product.storeName)
//            put("price", product.price)
//            put("imageResource", product.imageResource)
//            put("rating", product.rating)
//            put("category", product.category)
//            put("description", product.description)
//        }
//        val selection = "name = ?"
//        val selectionArgs = arrayOf(product.name)
//        return db.update("Product", values, selection, selectionArgs)
//    }
//
//    // Xóa sản phẩm
//    fun deleteProduct(name: String): Int {
//        val selection = "name = ?"
//        val selectionArgs = arrayOf(name)
//        return db.delete("Product", selection, selectionArgs)
//    }
}
