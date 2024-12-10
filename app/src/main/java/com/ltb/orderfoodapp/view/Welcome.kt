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
import com.ltb.orderfoodapp.data.dao.ProductDAO
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
        dbHelper = DatabaseHelper(this)
        db = dbHelper.writableDatabase
        productDAO = ProductDAO(this)
        checkLogin()
    }
    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        checkLogin()
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }


    fun checkLogin() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val role = sharedPreferences.getString("role", "guess")
        println(role)
        if (isLoggedIn) {
            if (role == "admin") {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        println("admin")
                        val home = Intent(this@Welcome, EditRole::class.java)
                        startActivity(home)
                        finish()
                    }
                }, 3000)
            } else if(role == "restaurant") {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        val intent = Intent(this@Welcome, SellerDashboardHome::class.java)
                        startActivity(intent)
                        finish()
                    }
                }, 3000)

            }
            else{
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        println("customer")
                        val home = Intent(this@Welcome, Home::class.java)
                        startActivity(home)
                        finish()
                    }
                }, 3000)
            }

        } else {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    val home = Intent(this@Welcome, Onboarding::class.java)
                    startActivity(home)
                    finish()
                }
            }, 3000)
        }
    }


}
