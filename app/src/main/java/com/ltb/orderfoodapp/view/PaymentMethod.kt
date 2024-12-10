package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.api.Payment
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart
import com.ltb.orderfoodapp.data.model.User
import com.vnpay.authentication.VNP_AuthenticationActivity
import vn.zalopay.sdk.ZaloPaySDK


class PaymentMethod : AppCompatActivity() {
    private lateinit var btnCash: ImageButton
    private lateinit var btnVNPay: ImageButton
    private lateinit var btnZaloPay: ImageButton
    private lateinit var payment: Payment
    private var pricePayment: Int = 0
    private lateinit var orderDAO: OrderDAO



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_method)
        payment = Payment()
        // Khởi tạo các ImageButton
        btnCash = findViewById(R.id.btn_cash)
        btnVNPay = findViewById(R.id.btn_vnPay)
        btnZaloPay = findViewById(R.id.btn_zaloPay)
        orderDAO = OrderDAO(this)

        pricePayment = intent.getIntExtra("pricePayment", 0)
        val txtPrice = findViewById<TextView>(R.id.totalPrice)
        txtPrice.text = pricePayment.toString()


        btnCash.setOnClickListener { setSelectedPaymentMethod(btnCash) }
        btnVNPay.setOnClickListener { setSelectedPaymentMethod(btnVNPay) }
        btnZaloPay.setOnClickListener {
            setSelectedPaymentMethod(btnZaloPay)


        }
        val backCart = findViewById<ImageButton>(R.id.backCart)
        val paymentConfirm = findViewById<Button>(R.id.paymentConfirm)
        // Lui ve Cart
        backCart.setOnClickListener {
            val Cart = Intent(this, MyCart::class.java)
            startActivity(Cart)
        }

        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val user = sharedPreferences.getString("user", "")
        val userObject = Gson().fromJson(user, User::class.java)


        val cartProductsJson = intent.getStringExtra("cartProductsJson")
        val gson = Gson()
        val cartProducts: List<ProductCart> = gson.fromJson(cartProductsJson, Array<ProductCart>::class.java).toList()



        val userId = userObject.getIdUser()
        val paymentSuccess = Intent(this, PaymentSuccess::class.java)
        paymentConfirm.setOnClickListener {
            if (btnCash.isSelected) {
                print("cash")

                orderDAO.addOrder(pricePayment, 1, userId, cartProducts.toMutableList())
                startActivity(paymentSuccess)
            } else if (btnVNPay.isSelected) {
                print("vnpay")
                payWithVNPay()
            } else if (btnZaloPay.isSelected) {
                print("zalopay")
                payment.payWithZaloPay(
                    amount = pricePayment,
                    onError = {
                        // xử lý khi có lỗi xảy ra
                        Toast.makeText(this, "Lỗi thanh toán", Toast.LENGTH_SHORT).show()
                    },
                    onSuccess = {
                        Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show()

                        orderDAO.addOrder(pricePayment, 3, userId, cartProducts.toMutableList())

                        // Chuyển đến màn hình thành công
                        startActivity(paymentSuccess)

                    },
                    onCancel = {
                        // xử lý khi người dùng hủy thanh toán
                        Toast.makeText(this, "Hủy thanh toán", Toast.LENGTH_SHORT).show()
                    },

                    context = this
                )
            } else println("don't get selected")

        }
    }


    // Chon phuong thuc tahnh toan
    private fun setSelectedPaymentMethod(selectedButton: ImageButton) {
        val txtCash = findViewById<TextView>(R.id.txtCash)
        val txtVNPay = findViewById<TextView>(R.id.txtVNPay)
        val txtZaloPay = findViewById<TextView>(R.id.txtZaloPay)
        btnCash.isSelected = false
        btnVNPay.isSelected = false
        btnZaloPay.isSelected = false
        txtCash.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        txtVNPay.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        txtZaloPay.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

        val iconCheck = ContextCompat.getDrawable(this, R.drawable.check)
        when (selectedButton) {
            btnCash -> {
                txtCash.setCompoundDrawablesWithIntrinsicBounds(null, null, iconCheck, null)
                btnCash.isSelected = true
            }

            btnVNPay -> {
                txtVNPay.setCompoundDrawablesWithIntrinsicBounds(null, null, iconCheck, null)
                btnVNPay.isSelected = true
            }

            btnZaloPay -> {
                txtZaloPay.setCompoundDrawablesWithIntrinsicBounds(null, null, iconCheck, null)
                btnZaloPay.isSelected = true
            }
        }
    }


    fun payWithVNPay() {
        val intent = Intent(
            this,
            VNP_AuthenticationActivity::class.java
        )
        intent.putExtra("url", "https://sandbox.vnpayment.vn/testsdk/") //bắt buộc, VNPAY cung cấp
        intent.putExtra("tmn_code", "FAHASA03") //bắt buộc, VNPAY cung cấp
        intent.putExtra(
            "scheme",
            "resultactivity"
        )
        intent.putExtra(
            "is_sandbox",
            false
        )
        VNP_AuthenticationActivity.setSdkCompletedCallback { action ->
            Log.wtf("SplashActivity", "action: $action")
            //action == AppBackAction
            //Người dùng nhấn back từ sdk để quay lại

            //action == CallMobileBankingApp
            //Người dùng nhấn chọn thanh toán qua app thanh toán (Mobile Banking, Ví...)
            //lúc này app tích hợp sẽ cần lưu lại cái PNR, khi nào người dùng mở lại app tích hợp thì sẽ gọi kiểm tra trạng thái thanh toán của PNR Đó xem đã thanh toán hay chưa.

            //action == WebBackAction
            //Người dùng nhấn back từ trang thanh toán thành công khi thanh toán qua thẻ khi url có chứa: cancel.sdk.merchantbackapp

            //action == FaildBackAction
            //giao dịch thanh toán bị failed

            //action == SuccessBackAction
            //thanh toán thành công trên webview
        }
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }


}