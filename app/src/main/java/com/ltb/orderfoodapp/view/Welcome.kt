package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.dao.ProductDAO

class Welcome : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var productDAO: ProductDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        dbHelper = DatabaseHelper(this)
        db = dbHelper.writableDatabase
        productDAO = ProductDAO(this)
        checkLogin()
    }
    fun checkLogin(){
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            lifecycleScope.launch {
                delay(3000)
                val userId = sharedPreferences.getString("userId", null)
                val home = Intent(this@Welcome, Home::class.java)
                home.putExtra("userId",userId)
                startActivity(home)
                finish()
            }

        } else {
            lifecycleScope.launch {
                delay(3000)
                val intent = Intent(this@Welcome, Onboarding::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}
