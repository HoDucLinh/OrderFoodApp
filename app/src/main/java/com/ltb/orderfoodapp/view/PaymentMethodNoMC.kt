package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R

class PaymentMethodNoMC : AppCompatActivity() {
    private lateinit var btnCash : ImageButton
    private lateinit var btnVNPay : ImageButton
    private  lateinit var btnZaloPay : ImageButton
    // Thêm các nút thanh toán khác nếu cần

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_method_no_mc)

        // Khởi tạo các ImageButton
        btnCash = findViewById(R.id.btn_cash)
        btnVNPay = findViewById(R.id.btn_vnPay)
        btnZaloPay = findViewById(R.id.btn_zaloPay)



        // Thêm các nút thanh toán khác nếu cần

        // Thiết lập sự kiện nhấn cho từng nút
        btnCash.setOnClickListener { setSelectedPaymentMethod(btnCash) }
        btnVNPay.setOnClickListener { setSelectedPaymentMethod(btnVNPay) }
        btnZaloPay.setOnClickListener { setSelectedPaymentMethod(btnZaloPay) }

        val backCart = findViewById<ImageButton>(R.id.backCart)
        val paymentConfirm = findViewById<Button>(R.id.paymentConfirm)
        // Lui ve Cart
        backCart.setOnClickListener{
            val Cart = Intent(this,MyCart::class.java)
            startActivity(Cart)
        }
        // Them card moi
        paymentConfirm.setOnClickListener{
            val addCard = Intent(this, AddCard::class.java)
            startActivity(addCard)
        }
    }

    // Hàm thiết lập trạng thái chọn cho một nút
    private fun setSelectedPaymentMethod(selectedButton: ImageButton) {
        // Đặt tất cả các nút về trạng thái không chọn
        btnCash.isSelected = false
        btnVNPay.isSelected = false
        btnZaloPay.isSelected = false
        // Đặt các nút khác về false nếu cần

        // Đặt nút được chọn
        selectedButton.isSelected = true
    }
}