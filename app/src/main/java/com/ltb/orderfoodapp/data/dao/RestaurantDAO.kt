package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.model.Restaurant

class RestaurantDAO() {
    private lateinit var db: SQLiteDatabase
    fun addRestaurant(restaurantName: String): Int {
        val existingRestaurantId = getRestaurantIdByName(restaurantName)
        if (existingRestaurantId != -1) {
            return existingRestaurantId
        }
        val values = ContentValues().apply {
            put("Name", restaurantName)
        }
        val restaurantId = db.insert("Restaurant", null, values)
        return restaurantId.toInt()
    }

    fun getRestaurantIdByName(restaurantName: String): Int {
        val cursor = db.query(
            "Restaurant",
            arrayOf("ID"),
            "Name = ?",
            arrayOf(restaurantName),
            null, null, null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
            cursor.close()
            return id
        }

        cursor?.close()
        return -1  // Trả về -1 nếu không tìm thấy Restaurant
    }


}
