package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.IntRect
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R

class AddCard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_card)
        val close = findViewById<ImageButton>(R.id.close)
        val addMakePayment = findViewById<Button>(R.id.addMakePayment)
        // Close tab
        close.setOnClickListener{
            val paymenMethodNoMC = Intent(this,PaymentMethodNoMC::class.java)
            startActivity(paymenMethodNoMC)
        }
        // Add Card
        addMakePayment.setOnClickListener{
            val paymentMethod = Intent(this,PaymentMethod::class.java)
            startActivity(paymentMethod)
        }

    }
}