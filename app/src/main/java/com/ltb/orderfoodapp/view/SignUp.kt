package com.ltb.orderfoodapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R


class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
//        signUpBtn.setOnClickListener{
//            val returnLogin = Intent( this, SignIn::class.java)
//            startActivity(returnLogin)
//        }
    }
}