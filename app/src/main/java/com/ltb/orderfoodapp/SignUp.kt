package com.ltb.orderfoodapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import kotlin.math.sign


class SignUp : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        signUpBtn.setOnClickListener{
            val returnLogin = Intent( this, SignIn::class.java)
            startActivity(returnLogin)
        }
    }
}