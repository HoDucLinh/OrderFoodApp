package com.ltb.orderfoodapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MyCart : AppCompatActivity() {

    private lateinit var productsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart) // Đảm bảo sử dụng đúng layout chính

        productsContainer = findViewById(R.id.productsContainer)

        // Giả sử bạn có số lượng LinearLayout mà bạn muốn chèn vào ScrollView
        for (i in 0 until 10) { // Ví dụ thêm 5 sản phẩm
            // Inflate layout cho từng sản phẩm
            val view: View = LayoutInflater.from(this).inflate(R.layout.item_product, productsContainer, false)

            // Thêm view vào productsContainer
            productsContainer.addView(view)
        }
    }
}
