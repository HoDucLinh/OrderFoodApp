package com.ltb.orderfoodapp.data.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Product

class CategoryDAO(context: Context) {
    private var db: SQLiteDatabase
    init {
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }
    // Hàm lấy toàn bộ danh sách Category
    fun getAllCategories(): MutableList<Category> {
        val categoryList = mutableListOf<Category>()

        val cursor = db.query(
            "Category",        // Tên bảng
            arrayOf("ID", "Name", "Description"), // Các cột cần lấy
            null, // Không có điều kiện
            null, // Không có giá trị điều kiện
            null, null, null  // Không group, không order
        )

        cursor.use {
            while (it.moveToNext()) {
                val category = Category(
                    idCategory = it.getInt(it.getColumnIndexOrThrow("ID")),
                    name = it.getString(it.getColumnIndexOrThrow("Name")),
                    description = it.getString(it.getColumnIndexOrThrow("Description"))
                )
                categoryList.add(category)
            }
        }

        return categoryList
    }

    // Hàm lay Product dựa trên Category_ID
    fun getProductByCategoryName(categoryName: String): MutableList<Product> {
        val productList = mutableListOf<Product>()
        val cursor = db.rawQuery("""
        SELECT 
            p.ID, p.Name, p.Price, p.Rating, p.Description, 
            c.Name AS CategoryName, r.Name AS RestaurantName, i.Value AS ImageSource 
        FROM Product p
        LEFT JOIN Category c ON p.Category_ID = c.ID
        LEFT JOIN Image i ON p.ID = i.Product_ID
        LEFT JOIN Restaurant r ON p.Restaurant_ID = r.ID
        WHERE c.Name = ?
    """, arrayOf(categoryName))
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
    // Lấy ID của Category theo tên
    fun getCategoryIdByName(categoryName: String): Int {
        val cursor = db.query(
            "Category",               // Tên bảng
            arrayOf("ID"),            // Cột cần lấy là ID
            "Name = ?",             // Điều kiện tìm kiếm theo tên Category
            arrayOf(categoryName),   // Giá trị của điều kiện
            null, null, null
        )

        var categoryId = -1
        cursor?.use {
            if (it.moveToFirst()) {
                categoryId = it.getInt(it.getColumnIndexOrThrow("ID"))
            }
        }
        return categoryId
    }
}
