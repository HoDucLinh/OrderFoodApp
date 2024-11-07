package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import com.ltb.orderfoodapp.R

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        lifecycleScope.launch {
            delay(3000)
            val intent = Intent(this@Welcome, Onboarding::class.java)
            startActivity(intent)
            finish()
        }
    }
}
