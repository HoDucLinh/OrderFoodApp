package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ProductCartAdapter
import com.ltb.orderfoodapp.data.dao.ProductCartDAO

class MyCart : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productCartAdapter: ProductCartAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_cart)

        // Tìm RecyclerView trong layout
        recyclerView = findViewById(R.id.recyclerViewCart)

        // Tạo DAO và lấy danh sách sản phẩm trong giỏ hàng
        val cartDAO = ProductCartDAO(this)
        val cartId = 1 // ID giỏ hàng, giả định là 1
        val productCartList = cartDAO.getAllProductsOfCart()

        // Đặt layout manager và adapter cho RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        productCartAdapter = ProductCartAdapter(this, productCartList)
        recyclerView.adapter = productCartAdapter
    }

    override fun onResume() {
        super.onResume()

        // Làm mới dữ liệu
        val cartDAO = ProductCartDAO(this)
        val productCartList = cartDAO.getAllProductsOfCart()

        // Cập nhật Adapter
        productCartAdapter.productCartList.clear()
        productCartAdapter.productCartList.addAll(productCartList)
        productCartAdapter.notifyDataSetChanged()
    }
}
