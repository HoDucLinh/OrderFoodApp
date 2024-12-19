package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.GroupOrderAdapter
import com.ltb.orderfoodapp.data.dao.OrderDAO
class OrderStatus : AppCompatActivity() {

    private lateinit var productListGridView: GridView
    private lateinit var orderDao: OrderDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)
        enableEdgeToEdge()
        productListGridView = findViewById(R.id.product)
        orderDao = OrderDAO(this)

        setupGridView()

        val back = findViewById<ImageButton>(R.id.backSeller)
        back.setOnClickListener {
            val seller = Intent(this, Menu:: class.java)
            startActivity(seller)
        }
    }

    override fun onResume() {
        super.onResume()
        setupGridView()
    }

    override fun onStart() {
        super.onStart()
        setupGridView()
    }
    private fun setupGridView() {
        val groupOrderAdapter = GroupOrderAdapter(
            context = this,
            orderDao = orderDao,
        )

        productListGridView.adapter = groupOrderAdapter
    }
}
