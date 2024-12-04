package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.api.AuthManager

class Menu : AppCompatActivity() {
    private lateinit var auth : AuthManager
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
        val logout = findViewById<TextView>(R.id.logout)
        btnfoodlist.setOnClickListener{
            val myfoodlist = Intent(this, MyFood::class.java)
            startActivity(myfoodlist)
        }
        logout.setOnClickListener{
            auth = AuthManager(this)
            auth.logout()
        }
        val statistics = findViewById<TextView>(R.id.statistics)
        statistics.setOnClickListener{
            val thongke = Intent(this, OrderStatistics::class.java)
            startActivity(thongke)
        }

    }
}