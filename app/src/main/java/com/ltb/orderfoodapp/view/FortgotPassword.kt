package com.ltb.orderfoodapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.api.AuthManager


class FortgotPassword : AppCompatActivity() {
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fortgot_password)
        val backSignIn = findViewById<ImageButton>(R.id.back)
        val btnLogin = findViewById<Button>(R.id.loginBtn)
        val sendCode = findViewById<Button>(R.id.sendCodeBtn)
        backSignIn.setOnClickListener{
            val backLogin = Intent(this, SignIn::class.java)
            startActivity(backLogin)
        }
        btnLogin.setOnClickListener{
            val backLogin = Intent(this, SignIn::class.java)
            startActivity(backLogin)
        }
        sendCode.setOnClickListener{
            val emailForgot = findViewById<TextInputLayout>(R.id.emailForgot).editText?.text.toString()
            if(emailForgot!=""){
                authManager = AuthManager(this)
                authManager.sendPasswordReset(emailForgot)
                Toast.makeText(this, "Send change password success", Toast.LENGTH_SHORT).show()
            }else Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show()

        }
    }
    public override fun onStart() {
        super.onStart()
    }
}