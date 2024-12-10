package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.annotation.IntegerRes
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Product

class CartDAO (context: Context){
    private val db: SQLiteDatabase = DatabaseHelper.getInstance(context).writableDatabase
    fun insertCart(totalNumber: Int, userId: Int): Long {
        val query = "SELECT * FROM Cart WHERE User_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        return if (cursor != null && cursor.moveToFirst()) {
            // Giỏ hàng đã tồn tại, trả về idCart
            val existingCartId = cursor.getLong(cursor.getColumnIndexOrThrow("ID"))
            cursor.close()
            existingCartId
        } else {
            // Không tồn tại, thêm giỏ hàng mới
            val values = ContentValues().apply {
                put("TotalNumber", totalNumber)
                put("User_ID", userId)
            }
            db.insert("Cart", null, values)
        }
    }





}