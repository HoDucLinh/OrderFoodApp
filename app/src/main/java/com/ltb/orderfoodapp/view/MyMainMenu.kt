package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R

class MyMainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_main_menu)
        val btnPersonalInfor = findViewById<TextView>(R.id.personalInfo)
        btnPersonalInfor.setOnClickListener{
            val personalInfor = Intent(this, PersonalInformation::class.java)
            startActivity(personalInfor)
        }
        val backHome = findViewById<ImageButton>(R.id.backHome)
        backHome.setOnClickListener{
            val home = Intent(this, Home::class.java)
            startActivity(home)
        }
        // Chuyen sang my orders
        val myOrders = findViewById<TextView>(R.id.myOrders)
        myOrders.setOnClickListener{
            val myOrders = Intent(this, MyOrder::class.java)
            startActivity(myOrders)
        }
        // Chuyen sang my orders
        val address = findViewById<TextView>(R.id.address)
        address.setOnClickListener{
            val editAddress = Intent(this, MyAddress::class.java)
            startActivity(editAddress)
        }
    }
}