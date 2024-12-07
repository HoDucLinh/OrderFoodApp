package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.media.Rating
import android.os.Build
import androidx.annotation.RequiresApi
import com.ltb.orderfoodapp.data.DatabaseHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RatingDAO(context: Context) {
    private var db: SQLiteDatabase
    init {
        val dbHelper = DatabaseHelper.getInstance(context)
        db = dbHelper.writableDatabase
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addRating(rating: Float, comment: String, productId: Int): Long {
        val currentDateTime = LocalDateTime.now()
        val formattedDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val values = ContentValues().apply {
            put("Rating", rating)
            put("Comment", comment)
            put("ReviewDate", formattedDate)
            put("Product_ID", productId)
        }

        return db.insert("ReviewOrder", null, values) // Trả về ID của dòng vừa được chèn, hoặc -1 nếu lỗi
    }
}