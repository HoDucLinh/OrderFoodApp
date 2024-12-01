package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Role
import com.ltb.orderfoodapp.data.model.User
import java.security.MessageDigest


class UserDAO(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper.getInstance(context).writableDatabase
//    init {
//        val admin = User(
//            fullName = "Admin",
//            email = "admin@orderfoodapp.com",
//            phoneNumber = "0123456789",
//            bioInfor = "Administrator account",
//            password = "admin123",
//        )
//        addUser(admin)
//    }


    fun addUser(user : User): Int {
        val passwordEncrypt = encrypt(user.password)
        val values = ContentValues().apply {
            put("FullName", user.fullName)
            put("Email", user.email)
            put("PhoneNumber", user.phoneNumber)
            put("BioInfor", user.bioInfor)
            put("Password",passwordEncrypt)
            put("Cart_ID",user.cartId)
            
        }
        val userId = db.insert("User", null, values).toInt()
        val roleValues = ContentValues().apply {
            put("RoleName", user.role)
            put("User_ID", userId)
        }
        db.insert("Role", null, roleValues)
        return userId
    }
    fun getUser(email: String, password: String): User? {
        val hashedPassword = encrypt(password)
        val query = """
        SELECT 
            u.ID AS userId, 
            u.FullName, 
            u.Email, 
            u.PhoneNumber, 
            u.BioInfor, 
            u.Password, 
            u.Cart_ID, 
            r.RoleName 
        FROM 
            User u 
        LEFT JOIN 
            Role r 
        ON 
            u.ID = r.User_ID 
        WHERE 
            u.Email = ? AND u.Password = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(email, hashedPassword))

        if (cursor != null && cursor.moveToFirst()) {
            val user = User(
                idUser = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow("FullName")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("Email")),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")),
                bioInfor = cursor.getString(cursor.getColumnIndexOrThrow("BioInfor")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("Password")),
                cartId = cursor.getInt(cursor.getColumnIndexOrThrow("Cart_ID")),
                role = cursor.getString(cursor.getColumnIndexOrThrow("RoleName")) ?: Role.CUSTOMER.toString() // Gán vai trò mặc định nếu không có
            )
            cursor.close()
            return user
        }

        cursor?.close()
        return null
    }


    fun encrypt(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
    fun decrypt(hexString: String): String {
        val byteArray = hexString.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        return String(byteArray, Charsets.UTF_8)
    }

}
