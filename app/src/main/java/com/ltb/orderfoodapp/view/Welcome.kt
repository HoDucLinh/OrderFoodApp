package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.dao.CategoryDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.User
import java.util.Timer
import java.util.TimerTask
class Welcome : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var productDAO: ProductDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        // Khởi tạo cơ sở dữ liệu
        dbHelper = DatabaseHelper(this)
        db = dbHelper.writableDatabase
        productDAO = ProductDAO(this)


//        setupNightMode()

        checkLogin()
    }

    private fun checkLogin() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val role = sharedPreferences.getString("role", "customer")

        val destination = when {
            isLoggedIn && role == "admin" -> EditRole::class.java
            isLoggedIn && role == "restaurant" -> SellerDashboardHome::class.java
            isLoggedIn -> Home::class.java
            else -> Onboarding::class.java
        }


        android.os.Handler(mainLooper).postDelayed({
            startActivity(Intent(this, destination))
            finish()
        }, 2000)
    }
}
