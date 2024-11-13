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
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product

class FoodDetail : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_food_detail)
//        getProductInfor()
        setProductInfor()
        val addToCart = findViewById<Button>(R.id.addCart)
        val backMain = findViewById<ImageView>(R.id.backMain)
        // Khai báo các biến
        val bttang = findViewById<ImageView>(R.id.buttontang)
        val btgiam = findViewById<ImageView>(R.id.buttongiam)
        val txtresult = findViewById<TextView>(R.id.txtSoLuong)

        // Khởi tạo giá trị ban đầu cho số lượng
        var soLuong = 0
        txtresult.text = soLuong.toString()

        // Xử lý sự kiện khi nhấn nút tăng
        bttang.setOnClickListener {
            soLuong += 1
            txtresult.text = soLuong.toString()
        }

        // Xử lý sự kiện khi nhấn nút giảm
        btgiam.setOnClickListener {
            if (soLuong > 0) {
                soLuong -= 1
            }
            txtresult.text = soLuong.toString()
        }

        backMain.setOnClickListener {
            val home = Intent(this, Home::class.java)
            startActivity(home)
        }
        addToCart.setOnClickListener {
            Toast.makeText(this, "Them thanh cong", Toast.LENGTH_SHORT).show()

        }

    }

//    fun getProductInfor() {
//        // Lấy giá trị từ Intent và gán cho các thuộc tính
//        val name = intent.getStringExtra("name") ?: ""
//        val storeName = intent.getStringExtra("storeName") ?: ""
//        val price = intent.getIntExtra("price", 0.0)
//        val imageResource = intent.getStringExtra("imageResource") ?: ""  // Chú ý: imageResource giờ là String
//        val rating = intent.getFloatExtra("rating", 0f)  // Sử dụng getFloatExtra thay vì getIntExtra
//        val category = intent.getStringExtra("category") ?: ""  // Thuộc tính category
//        val description = intent.getStringExtra("description") ?: ""  // Thuộc tính description
//
//        // Tạo đối tượng Product với các giá trị đã lấy
//        val product = Product(name, storeName, price, imageResource, rating, category, description)
//    }


    fun setProductInfor() {


    }
}