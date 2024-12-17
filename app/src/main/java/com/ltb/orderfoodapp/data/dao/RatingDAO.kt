package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.media.Rating
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Review
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RatingDAO(private val context: Context) {
    private lateinit var db: SQLiteDatabase



    @RequiresApi(Build.VERSION_CODES.O)
    fun addRating(rating: Float, comment: String, productId: Int, userId: Int): Long {
        val dbHelper = DatabaseHelper.getInstance(context)
        val db = dbHelper.writableDatabase

        // Lấy thời gian hiện tại
        val currentDateTime = LocalDateTime.now()
        val formattedDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        // Kiểm tra xem bản ghi đã tồn tại chưa
        val cursor = db.rawQuery(
            "SELECT * FROM ReviewOrder WHERE User_ID = ? AND Product_ID = ?",
            arrayOf(userId.toString(), productId.toString())
        )

        return if (cursor.moveToFirst()) {
            // Bản ghi tồn tại -> UPDATE
            val values = ContentValues().apply {
                put("Rating", rating)
                put("Comment", comment)
                put("ReviewDate", formattedDate)
            }
            db.update(
                "ReviewOrder",
                values,
                "User_ID = ? AND Product_ID = ?",
                arrayOf(userId.toString(), productId.toString())
            ).toLong()
        } else {
            // Bản ghi chưa tồn tại -> INSERT
            val values = ContentValues().apply {
                put("Rating", rating)
                put("Comment", comment)
                put("ReviewDate", formattedDate)
                put("Product_ID", productId)
                put("User_ID", userId)
            }
            db.insert("ReviewOrder", null, values)
        }.also {
            cursor.close()
        }
    }


    fun getReviewsForProduct(productId: Int): List<Review> {
        val dbHelper = DatabaseHelper.getInstance(context)
        db = dbHelper.readableDatabase
        val reviews = mutableListOf<Review>()
        val query = """
        SELECT 
            User.FullName, 
            ReviewOrder.Rating, 
            ReviewOrder.Comment, 
            ReviewOrder.ReviewDate 
        FROM 
            ReviewOrder
        JOIN 
            User 
        ON 
            ReviewOrder.User_ID = User.ID
        WHERE 
            ReviewOrder.Product_ID = ?
        ORDER BY 
            ReviewOrder.ReviewDate DESC
    """

        val cursor = db.rawQuery(query, arrayOf(productId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val fullName = cursor.getString(cursor.getColumnIndexOrThrow("FullName"))
                val displayName = if (fullName.isBlank()) "Anonymous" else fullName
                val rating = cursor.getInt(cursor.getColumnIndexOrThrow("Rating"))
                val comment = cursor.getString(cursor.getColumnIndexOrThrow("Comment"))
                val reviewDate = cursor.getString(cursor.getColumnIndexOrThrow("ReviewDate"))

                reviews.add(Review(displayName, rating, comment, reviewDate))
            } while (cursor.moveToNext())
        }

        cursor.close()
//        db.close()
        return reviews
    }


}
