package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.model.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Product

class ProductDAO(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private lateinit var db: SQLiteDatabase

    fun open() {
        db = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    // Thêm sản phẩm mới
    fun addProduct(product: Product): Long {
        val values = ContentValues().apply {
            put("name", product.name)
            put("storeName", product.storeName)
            put("price", product.price)
            put("imageResource", product.imageResource)
            put("rating", product.rating)
            put("category", product.category)
            put("description", product.description)
        }
        return db.insert("Product", null, values)
    }

    // Lấy danh sách tất cả sản phẩm
    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val cursor: Cursor = db.query("Product", null, null, null, null, null, null)
        cursor.use {
            while (it.moveToNext()) {
                val product = Product(
                    it.getString(it.getColumnIndexOrThrow("name")),
                    it.getString(it.getColumnIndexOrThrow("storeName")),
                    it.getInt(it.getColumnIndexOrThrow("price")),
                    it.getString(it.getColumnIndexOrThrow("imageResource")),
                    it.getFloat(it.getColumnIndexOrThrow("rating")),
                    it.getString(it.getColumnIndexOrThrow("category")),
                    it.getString(it.getColumnIndexOrThrow("description"))
                )
                productList.add(product)
            }
        }
        return productList
    }

    // Cập nhật sản phẩm
    fun updateProduct(product: Product): Int {
        val values = ContentValues().apply {
            put("name", product.name)
            put("storeName", product.storeName)
            put("price", product.price)
            put("imageResource", product.imageResource)
            put("rating", product.rating)
            put("category", product.category)
            put("description", product.description)
        }
        val selection = "name = ?"
        val selectionArgs = arrayOf(product.name)
        return db.update("Product", values, selection, selectionArgs)
    }

    // Xóa sản phẩm
    fun deleteProduct(name: String): Int {
        val selection = "name = ?"
        val selectionArgs = arrayOf(name)
        return db.delete("Product", selection, selectionArgs)
    }
}
