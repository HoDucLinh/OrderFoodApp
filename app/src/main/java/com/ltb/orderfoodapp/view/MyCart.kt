package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ProductCartAdapter
import com.ltb.orderfoodapp.data.api.Payment
import com.ltb.orderfoodapp.data.dao.CartDAO
import com.ltb.orderfoodapp.data.dao.ProductCartDAO
import com.ltb.orderfoodapp.data.model.ProductCart
import com.ltb.orderfoodapp.data.model.User
import com.ltb.orderfoodapp.viewmodel.ProductCartViewModel

class MyCart : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productCartAdapter: ProductCartAdapter
    private lateinit var productCartViewModel : ProductCartViewModel
    private var cartList : MutableList<ProductCart> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_cart)

        val backHome = findViewById<ImageButton>(R.id.backHome)
        val payment = findViewById<Button>(R.id.btnPayment)
        val total = findViewById<TextView>(R.id.total)

        // Lui ve home
        backHome.setOnClickListener {
            val home = Intent(this, Home::class.java)
            startActivity(home)
        }

        recyclerView = findViewById(R.id.recyclerViewCart)

        // Tạo DAO và lấy danh sách sản phẩm trong giỏ hàng
        productCartViewModel = ProductCartViewModel(this)
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val user = sharedPreferences.getString("user", "")
            val userObject = Gson().fromJson(user, User::class.java)
            val cartId = userObject.getCartId()
            cartList = productCartViewModel.getProductCartByCartID(cartId)
            for (productCart in cartList) {
                println(productCart.getName())
            }
            // Đặt layout manager và adapter cho RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)
            productCartAdapter = ProductCartAdapter(this, cartList)
            recyclerView.adapter = productCartAdapter

            // Tính và cập nhật tổng tiền
            val totalPrice = if (cartList.isEmpty()) {
                0
            } else {
                cartList.sumOf { it.getPrice() * it.getQuantity() }.toInt()
            }
            total.text = "$totalPrice VND"

            productCartAdapter.onQuantityChanged = { updatedTotalPrice ->
                runOnUiThread {
                    val totalPrice = if (productCartAdapter.productCartList.isEmpty()) {
                        0
                    } else {
                        updatedTotalPrice
                    }
                    total.text = "$totalPrice VND"
                }
            }
            val editAddress = findViewById<Button>(R.id.editAddress)
            val locationPath = intent.getStringExtra("locationPath")
            val address = findViewById<EditText>(R.id.addressUser)
            address.setText(locationPath)
            editAddress.setOnClickListener{
                address.isEnabled = true
                address.requestFocus()
            }


            // Chuyen toi payment
            payment.setOnClickListener {
                val paymentMethod = Intent(this, PaymentMethod::class.java)
                paymentMethod.putExtra("pricePayment", totalPrice)
                val gson = Gson()
                val cartProductsJson = gson.toJson(cartList)
                paymentMethod.putExtra("cartProductsJson", cartProductsJson)
                startActivity(paymentMethod)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val user = sharedPreferences.getString("user", "")
            val userObject = Gson().fromJson(user, User::class.java)
            val cartId = userObject.getCartId()
            println("CartIDasdfasd" + cartId)
            cartList = productCartViewModel.getProductCartByCartID(cartId)
        }

        // Cập nhật Adapter
        productCartAdapter.productCartList.clear()
        productCartAdapter.productCartList.addAll(cartList)
        productCartAdapter.notifyDataSetChanged()

        // Tính và cập nhật tổng tiền
        val total = findViewById<TextView>(R.id.total)
        if (cartList.isEmpty()) {
            total.text = "0 VND"
        } else {
            val totalPrice = cartList.sumOf { it.getPrice() * it.getQuantity() }.toInt()
            total.text = "$totalPrice VND"
        }
    }

}
