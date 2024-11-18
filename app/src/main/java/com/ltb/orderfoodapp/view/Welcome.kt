package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.DatabaseHelper

class Welcome : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        dbHelper = DatabaseHelper(this)
        dbHelper.writableDatabase
//        dbHelper.readableDatabase

        lifecycleScope.launch {
            delay(3000)
            val intent = Intent(this@Welcome, Onboarding::class.java)
            startActivity(intent)
            finish()
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
