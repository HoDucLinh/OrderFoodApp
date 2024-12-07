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
            put("Role_ID", user.roleId)
            
        }
        val userId = db.insert("User", null, values).toInt()
        return userId
    }
    fun getAllUsers(): MutableList<User> {
        val userList = mutableListOf<User>()
        val cursor = db.rawQuery("SELECT * FROM User", null)

        cursor.use {
            while (it.moveToNext()) {
                val user = User(
                    idUser = it.getInt(it
                        .getColumnIndexOrThrow("ID")),
                    fullName = it.getString(it.getColumnIndexOrThrow("FullName")),
                    email = it.getString(it.getColumnIndexOrThrow("Email")),
                    phoneNumber = it
                        .getString(it.getColumnIndexOrThrow("PhoneNumber")),
                    bioInfor = it
                        .getString(it.getColumnIndexOrThrow("BioInfor")),
                    password = it
                        .getString(it.getColumnIndexOrThrow("Password")),
                    cartId = it
                        .getInt(it.getColumnIndexOrThrow("Cart_ID")),
                    roleId = cursor.getInt(cursor.getColumnIndexOrThrow("Role_ID"))
                )
                userList.add(user)
            }
        }
        return userList
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
            u.Role_ID,
            r.RoleName,
            r.ID as RoleID
        FROM 
            User u 
        LEFT JOIN 
            Role r 
        ON 
            u.Role_ID = r.ID
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
                roleId = cursor.getInt(cursor.getColumnIndexOrThrow("RoleID"))
            )
            cursor.close()
            return user
        }

        cursor?.close()
        return null
    }

    fun updateUserCartId(userId: Int, cartId: Int): Int {
        val values = ContentValues().apply {
            put("Cart_ID", cartId)
        }
        return db.update("User", values, "ID = ?", arrayOf(userId.toString()))
    }

    fun deleteUser(email :String){
        val query = """
            DELETE FROM user
            WHERE Email = ? 
        """.trimIndent()
        db.execSQL(query)
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

    // Cập nhật thông tin người dùng
    fun updateRole(userId: Int, userRoleId: Int): Int {
        val values = ContentValues().apply {
            put("Role_ID", userRoleId)
        }
        // Cập nhật roleId cho người dùng có userId tương ứng
        return db.update("User", values, "ID = ?", arrayOf(userId.toString()))
    }


    // Xóa người dùng
    fun deleteUser(userId: Int): Boolean {
        return db.delete("User", "id = ?", arrayOf(userId.toString())) > 0
    }

}
