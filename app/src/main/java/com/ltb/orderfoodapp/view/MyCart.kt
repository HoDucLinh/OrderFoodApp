package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R

class MyCart : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_cart)
        val backHome = findViewById<ImageButton>(R.id.backHome)
        val payment = findViewById<Button>(R.id.btnPayment)
        val editItems = findViewById<Button>(R.id.editItems)
        // Lui ve home
        backHome.setOnClickListener{
            val home = Intent(this,Home::class.java)
            startActivity(home)
        }
        // Chueen toi payment
        payment.setOnClickListener{
            val payment = Intent(this, PaymentMethodNoMC::class.java)
            startActivity(payment)
        }
        // Chinh sua item

        editItems.setOnClickListener {
            val parentLayout: ViewGroup = findViewById(R.id.parentLayout)
            val imageButtonsWithTag = mutableListOf<ImageButton>()

            // Duyệt qua tất cả các View con trong parentLayout
            for (i in 0 until parentLayout.childCount) {
                val child = parentLayout.getChildAt(i)
                if (child is ImageButton && child.tag == "cancelButton") {
                    imageButtonsWithTag.add(child)
                    Log.d("ImageButtonTag", "Found ImageButton with tag: cancelButton")
                }
            }

            // Kiểm tra nếu danh sách không rỗng, sau đó đặt visibility
            if (imageButtonsWithTag.isNotEmpty()) {
                for (imageButton in imageButtonsWithTag) {
                    imageButton.visibility = View.VISIBLE
                }
            } else {
                Log.d("ImageButtonTag", "No ImageButton found with tag: cancelButton")
            }
        }
        val bttang = findViewById<ImageButton>(R.id.bttang)
        val btgiam = findViewById<ImageView>(R.id.btgiam)
        val txtresult = findViewById<TextView>(R.id.soluong)

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





    }
}