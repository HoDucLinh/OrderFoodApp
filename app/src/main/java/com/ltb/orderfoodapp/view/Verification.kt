package com.ltb.orderfoodapp.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R


class Verification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verification)

    }
    public override fun onStart() {
        super.onStart()
    }
}