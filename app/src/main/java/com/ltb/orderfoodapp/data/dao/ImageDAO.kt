package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Image

class ImageDAO(private val context: Context) {
    private lateinit var  db: SQLiteDatabase
    lateinit var storage: FirebaseStorage

    // Lấy danh sách hình ảnh theo Product_ID
    fun getImagesByProductId(productId: Int): List<Image> {
        db = DatabaseHelper.getInstance(context).writableDatabase
        val cursor = db.query(
            "Image",
            arrayOf("ID", "Value", "Product_ID"),
            "Product_ID = ?",
            arrayOf(productId.toString()),
            null, null, null
        )
        val images = mutableListOf<Image>()
        cursor.use {
            while (it?.moveToNext() == true) {
                images.add(
                    Image(
                        idImage = it.getInt(it.getColumnIndexOrThrow("ID")),
                        value = it.getString(it.getColumnIndexOrThrow("Value")),
                        productId = it.getInt(it.getColumnIndexOrThrow("Product_ID"))
                    )
                )
            }
        }
        return images
    }

    // Thêm hình ảnh vào bảng Image
    fun addImage(image : String, productId: Int): Long {
        db = DatabaseHelper.getInstance(context).writableDatabase
        val values = ContentValues().apply {
            put("Value", image)
            put("Product_ID", productId)
        }
        return db.insert("Image", null, values)
    }

    fun deleteImagesForProduct(productId: Int) {
        db = DatabaseHelper.getInstance(context).writableDatabase
        db.delete("Image", "Product_ID = ?", arrayOf(productId.toString()))
    }

}
