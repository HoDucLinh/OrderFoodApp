package com.ltb.orderfoodapp.data.dao

import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.model.Category

class CategoryDAO(private val db: SQLiteDatabase) {

    // Hàm này lấy Category dựa trên Category_ID
    fun getCategoryById(categoryId: Int): Category? {
        val cursor = db.query(
            "Category",  // Tên bảng Category
            arrayOf("ID", "Value", "Description"),  // Các cột cần lấy
            "ID = ?",  // Điều kiện lọc theo Category_ID
            arrayOf(categoryId.toString()),  // Truyền Category_ID vào câu truy vấn
            null, null, null
        )

        var category: Category? = null
        if (cursor != null && cursor.moveToFirst()) {
            category = Category(
                idCategory = cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("Value")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("Description"))
            )
        }
        cursor?.close()
        return category
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
