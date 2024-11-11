package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R

class MyAddress : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_address)
        val addAddress = findViewById<Button>(R.id.addAddress)
        addAddress.setOnClickListener{
            val address  = Intent(this, AddNewAddress::class.java)
            startActivity(address)
        }
        val backHome = findViewById<ImageButton>(R.id.backHome)
        backHome.setOnClickListener{
            val myMainMenu = Intent(this, MyMainMenu::class.java)
            startActivity(myMainMenu)
        }
    }
}