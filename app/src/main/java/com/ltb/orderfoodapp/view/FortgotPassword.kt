package com.ltb.orderfoodapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R


class FortgotPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fortgot_password)
//        val backSignIn = findViewById<Button>(R.id.backSignIn)
        val sendCode = findViewById<Button>(R.id.sendCodeBtn)
//        backSignIn.setOnClickListener{
//            val backLogin = Intent(this, SignIn::class.java)
//            startActivity(backLogin)
//        }
        sendCode.setOnClickListener{
            val emailForgot = findViewById<EditText>(R.id.userForgot).getText().toString()
            if(emailForgot!=""){
                val verification = Intent(this, Verification::class.java)
                startActivity(verification)
            }else Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show()

        }
    }
    public override fun onStart() {
        super.onStart()
        val emailForgot = findViewById<EditText>(R.id.userForgot)
    }
}