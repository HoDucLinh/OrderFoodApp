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
        db = dbHelper.writableDatabase
        // Lấy thời gian hiện tại
        val currentDateTime = LocalDateTime.now()
        val formattedDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        // Chuẩn bị dữ liệu để chèn
        val values = ContentValues().apply {
            put("Rating", rating)
            put("Comment", comment)
            put("ReviewDate", formattedDate)
            put("Product_ID", productId)
            put("User_ID", userId) // Thêm User_ID
        }

        return db.insert("ReviewOrder", null, values)
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
    fun getAverageRatingForAllProducts(): Float {
        val dbHelper = DatabaseHelper.getInstance(context)
        db = dbHelper.readableDatabase
        val ratings = mutableListOf<Float>()

        val query = """
    SELECT 
        ReviewOrder.Rating
    FROM 
        ReviewOrder
    """

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val rating = cursor.getInt(cursor.getColumnIndexOrThrow("Rating")).toFloat()
                ratings.add(rating)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return if (ratings.isNotEmpty()) {
            ratings.average().toFloat()
        } else {
            0f
        }
    }
    fun getAllReviews(): MutableList<Review> {
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
    ORDER BY 
        ReviewOrder.ReviewDate DESC
    """

        val cursor = db.rawQuery(query, null)  // Không cần tham số productId nữa

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
//    db.close()
        return reviews
    }

    fun getReviewsByUserId(userId: Int): MutableList<Review> {
        val dbHelper = DatabaseHelper.getInstance(context)
        db = dbHelper.readableDatabase
        val reviews = mutableListOf<Review>()

        // Modify the query to filter by User_ID
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
            ReviewOrder.User_ID = ?
        ORDER BY 
            ReviewOrder.ReviewDate DESC
    """

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))  // Pass the userId as a parameter

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
        return reviews
    }


    fun close() {
        db.close()

    }
}
