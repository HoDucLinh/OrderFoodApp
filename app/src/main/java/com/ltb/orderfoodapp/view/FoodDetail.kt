package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product

class FoodDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_food_detail)
        getProductInfor()
        val addToCart = findViewById<Button>(R.id.addCart)
        val backMain = findViewById<ImageView>(R.id.backMain)
        val bttang = findViewById<ImageView>(R.id.buttontang)
        val btgiam = findViewById<ImageView>(R.id.buttongiam)
        val txtresult = findViewById<TextView>(R.id.txtSoLuong)
        val priceTextView = findViewById<TextView>(R.id.priceTotal)
        var soLuong = 1
        var unitPrice = intent.getIntExtra("price", 0)
        txtresult.text = soLuong.toString()
        priceTextView.text = "${unitPrice * soLuong} VND"

        bttang.setOnClickListener {
            soLuong += 1
            txtresult.text = soLuong.toString()
            priceTextView.text = "${unitPrice * soLuong} VND"
        }

        btgiam.setOnClickListener {
            if (soLuong > 1) {
                soLuong -= 1
                txtresult.text = soLuong.toString()
                priceTextView.text = "${unitPrice * soLuong} VND"
            }
        }

        backMain.setOnClickListener {
            val home = Intent(this, Home::class.java)
            startActivity(home)
        }
        addToCart.setOnClickListener {
            Toast.makeText(this, "Them thanh cong", Toast.LENGTH_SHORT).show()

        }

    }

    fun getProductInfor() {
        // Lấy giá trị từ Intent và gán cho các thuộc tính
        val name = intent.getStringExtra("name") ?: ""
        val storeName = intent.getStringExtra("storeName") ?: ""
        val price = intent.getIntExtra("price", 0)
        val imageResource = intent.getStringArrayListExtra("imageResource") ?: arrayListOf()  // Nhận mảng chuỗi
        val rating = intent.getFloatExtra("rating", 0f)  // Sử dụng getFloatExtra thay vì getIntExtra
        val category = intent.getStringExtra("category") ?: ""  // Thuộc tính category
        val description = intent.getStringExtra("description") ?: ""  // Thuộc tính description
        // Lấy các phần tử trong giao diện
        val productNameTextView = findViewById<TextView>(R.id.productName)
        val storeNameTextView = findViewById<TextView>(R.id.restaurantName)
        val priceTextView = findViewById<TextView>(R.id.priceTotal)
        val ratingTextView = findViewById<TextView>(R.id.productRating)
        val descriptionTextView = findViewById<TextView>(R.id.productDes)
        val imageView = findViewById<ImageView>(R.id.imageProduct)

        // Gán các giá trị vào các phần tử
        productNameTextView.text = name
        storeNameTextView.text = storeName
        priceTextView.text = "${price}"
        ratingTextView.text = "$rating"
        descriptionTextView.text = description
        Glide.with(this).load(imageResource[0]).into(imageView)
    }
}