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
    private lateinit var btnCash: ImageButton
    private lateinit var btnVisa: ImageButton
    private lateinit var btnMasterCard: ImageButton
    private lateinit var btnPaypal: ImageButton
    // Thêm các nút thanh toán khác nếu cần

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method_no_mc)

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

        val backCart = findViewById<ImageButton>(R.id.backCart)
        val addNew = findViewById<Button>(R.id.addNewCard)
        // Lui ve Cart
        backCart.setOnClickListener{
            val Cart = Intent(this,MyCart::class.java)
            startActivity(Cart)
        }
        // Them card moi
        addNew.setOnClickListener{
            val addCard = Intent(this, AddCard::class.java)
            startActivity(addCard)
        }
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