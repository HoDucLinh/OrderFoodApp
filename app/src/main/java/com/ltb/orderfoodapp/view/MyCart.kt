package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R

class MyCart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_cart)
        val backHome = findViewById<ImageButton>(R.id.backHome)
        val payment = findViewById<Button>(R.id.btnPayment)
        val editItems = findViewById<Button>(R.id.editItems)
        // Lui ve home
        backHome.setOnClickListener{
            val home = Intent(this,Home::class.java)
            startActivity(home)
        }
        // Chueen toi payment
        payment.setOnClickListener{
            val payment = Intent(this, PaymentMethodNoMC::class.java)
            startActivity(payment)
        }
        // Chinh sua item
        editItems.setOnClickListener{


        }


    }
}