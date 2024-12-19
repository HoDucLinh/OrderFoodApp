package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Product

class CategoryDAO(private val context: Context) {
    private lateinit var db: SQLiteDatabase
    init {

    }
    // Hàm lấy toàn bộ danh sách Category
    fun getAllCategories(): MutableList<Category> {
        val dbHelper = DatabaseHelper.getInstance(context)
        db = dbHelper.readableDatabase
        val categoryList = mutableListOf<Category>()

        val cursor = db.query(
            "Category",
            arrayOf("ID", "Name", "Description"),
            null,
            null,
            null, null, null
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
        val dbHelper = DatabaseHelper.getInstance(context)
        db = dbHelper.readableDatabase
        val productList = mutableListOf<Product>()
        val cursor = db.rawQuery("""
        SELECT 
            p.ID, p.Name, p.Price, p.Rating, p.Description, 
            c.Name AS CategoryName, i.Value AS ImageSource 
        FROM Product p
        LEFT JOIN Category c ON p.Category_ID = c.ID
        LEFT JOIN Image i ON p.ID = i.Product_ID
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
//                val restaurant = it.getString(it.getColumnIndexOrThrow("RestaurantName"))
//                product.setRestaurant(restaurant)
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

        return productList
    }
    // Lấy ID của Category theo tên
    fun getCategoryIdByName(categoryName: String): Int {
        val dbHelper = DatabaseHelper.getInstance(context)
        db = dbHelper.readableDatabase
        val cursor = db.query(
            "Category",
            arrayOf("ID"),
            "Name = ?",
            arrayOf(categoryName),
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

    fun addCategory(name: String, description: String): Long {
        val dbHelper = DatabaseHelper.getInstance(context)
        db = dbHelper.writableDatabase

        val cursor = db.query(
            "Category",
            null,
            "Name = ? AND Description = ?",
            arrayOf(name, description),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            cursor.close()
            return -1L
        }
        cursor.close()

        // Chèn dữ liệu mới
        val values = ContentValues().apply {
            put("Name", name)
            put("Description", description)
        }
        return db.insert("Category", null, values)
    }
    fun updateCategory(currentName: String, newName: String, newDescription: String): Boolean {
        val dbHelper = DatabaseHelper.getInstance(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("Name", newName)
            put("Description", newDescription)
        }

        // Cập nhật dựa trên tên hiện tại của danh mục
        val rowsAffected = db.update("Category", values, "Name = ?", arrayOf(currentName))
        db.close()
        return rowsAffected > 0
    }



    fun deleteCategory(category: String) {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val deleteProductsQuery = """
        DELETE FROM Product
        WHERE Category_ID = (
            SELECT id FROM Category WHERE Name = ?
        )
    """
        db.execSQL(deleteProductsQuery, arrayOf(category))

        val deleteCategoryQuery = """
        DELETE FROM Category
        WHERE Name = ?
    """
        // Thực thi câu truy vấn để xóa category
        db.execSQL(deleteCategoryQuery, arrayOf(category))

        db.close()
    }

}
