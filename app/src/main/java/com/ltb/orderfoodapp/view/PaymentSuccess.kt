package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R

class PaymentSuccess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_success)
        val trackingOrder = findViewById<Button>(R.id.trackOrder)
        // Chuyen qua tracking Order
        trackingOrder.setOnClickListener {
            val trackingOrder = Intent(this, TrackingOrder::class.java)
            startActivity(trackingOrder)
        }
    }

}