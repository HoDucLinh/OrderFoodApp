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
import com.ltb.orderfoodapp.viewmodel.OrderViewModel

class SellerDashboardHome : AppCompatActivity() {
    private lateinit var orderViewModel: OrderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seller_dashboard_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val menu = findViewById<ImageButton>(R.id.nextMenu)
        val orders = findViewById<TextView>(R.id.totalOrders)
        val runningOrders = findViewById<TextView>(R.id.runningOrders)
        val revenue = findViewById<TextView>(R.id.totalRevenue)
        menu.setOnClickListener{
            val next_menu = Intent(this , Menu::class.java)
            startActivity(next_menu)
        }
        orderViewModel = OrderViewModel(this)
        orders.text = orderViewModel.getTotalOrders().toString()
        runningOrders.text = orderViewModel.getRunningOrdersCount().toString()
        revenue.text = orderViewModel.getRevenue().toString()




    }
}