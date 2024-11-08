package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R

class MyAddress : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_address)
        val addAddress = findViewById<Button>(R.id.addAddress)
        addAddress.setOnClickListener{
            val address  = Intent(this, AddNewAddress::class.java)
            startActivity(address)
        }
    }
}