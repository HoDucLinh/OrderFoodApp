package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.api.AuthManager
import com.ltb.orderfoodapp.viewmodel.OrderViewModel

class AdminDashboardHome : AppCompatActivity() {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var auth : AuthManager
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val orders = findViewById<TextView>(R.id.totalOrders)
        val runningOrders = findViewById<TextView>(R.id.runningOrders)
        val revenue = findViewById<TextView>(R.id.totalRevenue)
        val editUser = findViewById<Button>(R.id.editUser)
        val statistics = findViewById<Button>(R.id.btnStatistics)
        val logout = findViewById<Button>(R.id.btnLogOut)

        editUser.setOnClickListener{
            val edit = Intent(this , EditRole::class.java)
            startActivity(edit)
        }

        statistics.setOnClickListener{
            val thongke = Intent(this, StatisticsFragment::class.java)
            startActivity(thongke)
        }

        logout.setOnClickListener{
            auth = AuthManager(this)
            auth.logout()
        }

        orderViewModel = OrderViewModel(this)
        orders.text = orderViewModel.getTotalOrders().toString()
        runningOrders.text = orderViewModel.getRunningOrdersCount().toString()
        revenue.text = orderViewModel.getRevenue().toString()
    }
}