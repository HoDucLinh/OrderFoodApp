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

class PersonalInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personal_information)
        val backMenu = findViewById<ImageButton>(R.id.backMenu)
        backMenu.setOnClickListener{
            val mainMenu = Intent(this,MyMainMenu::class.java)
            startActivity(mainMenu)

        }
        // Chuyen sang tab edit
        val editInfor = findViewById<TextView>(R.id.editInfor)
        editInfor.setOnClickListener{
            val editInfor = Intent(this,EditProfile::class.java)
            startActivity(editInfor)

        }

    }
}