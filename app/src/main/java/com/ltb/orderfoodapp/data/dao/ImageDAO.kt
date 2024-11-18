package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.model.Image

class ImageDAO(private val db: SQLiteDatabase) {

    // Lấy danh sách hình ảnh theo Product_ID
    fun getImagesByProductId(productId: Int): List<Image> {
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
        val values = ContentValues().apply {
            put("Value", image)
            put("Product_ID", productId)
        }
        return db.insert("Image", null, values)
    }
}
