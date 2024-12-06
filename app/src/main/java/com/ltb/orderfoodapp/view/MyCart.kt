package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ProductCartAdapter
import com.ltb.orderfoodapp.data.api.Payment
import com.ltb.orderfoodapp.data.dao.ProductCartDAO

class MyCart : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productCartAdapter: ProductCartAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_cart)

        val backHome = findViewById<ImageButton>(R.id.backHome)
        val payment = findViewById<Button>(R.id.btnPayment)
        val total = findViewById<TextView>(R.id.total)

        // Lui ve home
        backHome.setOnClickListener{
            val home = Intent(this,Home::class.java)
            startActivity(home)
        }


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

        // Tính và cập nhật tổng tiền
        val totalPrice = productCartList.sumOf { it.price * it.quantity }.toInt()
        total.text = "$totalPrice VND"

        productCartAdapter.onQuantityChanged = { updatedTotalPrice ->
            runOnUiThread {
                total.text = "$updatedTotalPrice VND"
            }
        }
        // Chuyen toi payment
        payment.setOnClickListener{
            val paymentMethod = Intent(this, PaymentMethod::class.java)
            paymentMethod.putExtra("pricePayment" , totalPrice)
            startActivity(paymentMethod)
        }
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

        // Tính và cập nhật tổng tiền
        val totalPrice = productCartList.sumOf { it.price * it.quantity }.toInt()
        val total = findViewById<TextView>(R.id.total)
        total.text = "$totalPrice VND"
    }

}
