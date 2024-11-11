package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R

class AddNewAddress : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_address)
        val saveInfor = findViewById<Button>(R.id.saveInfor)
        saveInfor.setOnClickListener {
            val personalInformation = Intent(this, PersonalInformation::class.java)
            startActivity(personalInformation)
        }
        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            val myAddress = Intent(this, MyAddress::class.java)
            startActivity(myAddress)
        }
        val btHome = findViewById<Button>(R.id.btHome)
        val btWork = findViewById<Button>(R.id.btWork)
        val btOther = findViewById<Button>(R.id.btOther)

        // Lấy ColorStateList từ tài nguyên màu
        val orangeColorStateList = AppCompatResources.getColorStateList(this, R.color.orange)
        val defaultColorStateList = AppCompatResources.getColorStateList(this, R.color.gray_default)

        btHome.setOnClickListener {
            btHome.backgroundTintList = orangeColorStateList
            btWork.backgroundTintList = defaultColorStateList
            btOther.backgroundTintList = defaultColorStateList
        }

        btWork.setOnClickListener {
            btWork.backgroundTintList = orangeColorStateList
            btHome.backgroundTintList = defaultColorStateList
            btOther.backgroundTintList = defaultColorStateList
        }

        btOther.setOnClickListener {
            btOther.backgroundTintList = orangeColorStateList
            btHome.backgroundTintList = defaultColorStateList
            btWork.backgroundTintList = defaultColorStateList
        }


    }
}