package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R

class MyMainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_main_menu)
        val btnPersonalInfor = findViewById<TextView>(R.id.personalInfo)
        btnPersonalInfor.setOnClickListener{
            val personalInfor = Intent(this, PersonalInformation::class.java)
            startActivity(personalInfor)
        }
    }
}