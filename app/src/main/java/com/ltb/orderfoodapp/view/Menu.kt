package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R

class Menu : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ltb.orderfoodapp.R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.ltb.orderfoodapp.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnfoodlist = findViewById<TextView>(R.id.myfoodlist)
        btnfoodlist.setOnClickListener{
            val myfoodlist = Intent(this, MyFood::class.java)
            startActivity(myfoodlist)
        }
        val statistics = findViewById<TextView>(R.id.statistics)
        statistics.setOnClickListener{
            val thongke = Intent(this, OrderStatistics::class.java)
            startActivity(thongke)
        }

    }
}