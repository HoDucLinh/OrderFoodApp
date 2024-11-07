package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R

class TrackingOrder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tracking_order)
        val backHome = findViewById<ImageButton>(R.id.backHome)
        // chuyen ve home
        backHome.setOnClickListener{
            val backHome = Intent(this,Home::class.java)
            startActivity(backHome)
        }
    }
}