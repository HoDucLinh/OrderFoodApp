package com.ltb.orderfoodapp

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentMethod : AppCompatActivity() {
    private lateinit var btnCash: ImageButton
    private lateinit var btnVisa: ImageButton
    private lateinit var btnMasterCard: ImageButton
    private lateinit var btnPaypal: ImageButton
    // Thêm các nút thanh toán khác nếu cần

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)

        // Khởi tạo các ImageButton
        btnCash = findViewById(R.id.btn_cash)
        btnVisa = findViewById(R.id.btn_visa)
        btnMasterCard = findViewById(R.id.btn_mastercard)
        btnPaypal = findViewById(R.id.btn_paypal)

        // Thêm các nút thanh toán khác nếu cần

        // Thiết lập sự kiện nhấn cho từng nút
        btnCash.setOnClickListener { setSelectedPaymentMethod(btnCash) }
        btnVisa.setOnClickListener { setSelectedPaymentMethod(btnVisa) }
        btnMasterCard.setOnClickListener { setSelectedPaymentMethod(btnMasterCard) }
        btnPaypal.setOnClickListener { setSelectedPaymentMethod(btnPaypal) }

        // Thiết lập sự kiện nhấn cho các nút khác nếu có
    }

    // Hàm thiết lập trạng thái chọn cho một nút
    private fun setSelectedPaymentMethod(selectedButton: ImageButton) {
        // Đặt tất cả các nút về trạng thái không chọn
        btnCash.isSelected = false
        btnVisa.isSelected = false
        btnMasterCard.isSelected = false
        btnPaypal.isSelected = false
        // Đặt các nút khác về false nếu cần

        // Đặt nút được chọn
        selectedButton.isSelected = true
    }
}