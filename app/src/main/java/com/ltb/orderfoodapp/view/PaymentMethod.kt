package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.ltb.orderfoodapp.data.dao.ProductCartDAO
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
                orderDAO.addOrder(pricePayment, 1, userId, cartProducts.toMutableList())
                deleteProductsFromCart(cartProducts)
                startActivity(paymentSuccess)
            } else if (btnVNPay.isSelected) {
                print("vnpay")
                Toast.makeText(this, "Tính năng đang phát triển thử lại sau", Toast.LENGTH_SHORT).show()
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

                        orderDAO.addOrder(pricePayment, 1, userId, cartProducts.toMutableList())
                        deleteProductsFromCart(cartProducts)
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

    fun deleteProductsFromCart(cartProducts: List<ProductCart>) {
        val cartDAO = ProductCartDAO(this) // Giả sử bạn có một DBHelper để tương tác với CSDL
        for (product in cartProducts) {
            // Xóa từng sản phẩm trong giỏ hàng sau khi thanh toán
            cartDAO.deleteProduct(product.getProductId())
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


//    fun payWithVNPay() {
//        val intent = Intent(this, VNP_AuthenticationActivity::class.java)
//
//
//        // Truyền các tham số cần thiết cho VNPay
//        intent.putExtra("url", "https://sandbox.vnpayment.vn/testsdk/")
//        intent.putExtra("tmn_code", "FAHASA03")
//        intent.putExtra("scheme", "resultactivity")  // Scheme để nhận phản hồi
//        intent.putExtra("is_sandbox", true)  // Đặt là true cho môi trường sandbox, false cho môi trường sản phẩm
//
//        // Cài đặt callback cho kết quả từ SDK
//        VNP_AuthenticationActivity.setSdkCompletedCallback { action ->
//            when (action) {
//                "AppBackAction" -> {
//                    // Người dùng nhấn nút quay lại từ màn hình SDK
//                    Log.wtf("VNPay", "Người dùng nhấn nút quay lại")
//                    // Xử lý khi người dùng nhấn quay lại
//                }
//                "CallMobileBankingApp" -> {
//                    // Người dùng chọn thanh toán qua ứng dụng ngân hàng di động hoặc ví điện tử
//                    Log.wtf("VNPay", "Người dùng chọn thanh toán qua ứng dụng ngân hàng di động hoặc ví điện tử")
//                    // Xử lý khi người dùng chọn thanh toán qua ví điện tử hoặc ngân hàng di động
//                    // Bạn có thể lưu PNR để kiểm tra sau
//                }
//                "WebBackAction" -> {
//                    // Người dùng nhấn quay lại từ trang thanh toán thành công
//                    Log.wtf("VNPay", "Người dùng nhấn quay lại từ trang thành công")
//                    // Xử lý hành động quay lại trang web
//                }
//                "FaildBackAction" -> {
//                    // Thanh toán thất bại
//                    Log.wtf("VNPay", "Thanh toán thất bại")
//                    // Xử lý khi thanh toán thất bại
//                }
//                "SuccessBackAction" -> {
//                    // Thanh toán thành công
//                    Log.wtf("VNPay", "Thanh toán thành công")
//                    // Xử lý khi thanh toán thành công
//                    // Bạn có thể gọi API để xác nhận thanh toán
//                }
//                else -> {
//                    // Hành động không xác định
//                    Log.wtf("VNPay", "Hành động không xác định: $action")
//                    // Xử lý khi gặp hành động không xác định
//                }
//            }
//        }
//        startActivity(intent)
//    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }


}