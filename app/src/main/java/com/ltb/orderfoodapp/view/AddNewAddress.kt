package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R

class AddNewAddress : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_address)
        val saveInfor = findViewById<Button>(R.id.saveInfor)
        saveInfor.setOnClickListener{
            val personalInformation = Intent(this, PersonalInformation::class.java)
            startActivity(personalInformation)
        }
    }
}