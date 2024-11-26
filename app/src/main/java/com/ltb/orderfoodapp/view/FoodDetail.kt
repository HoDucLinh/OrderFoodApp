package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.ProductCartDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart

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
        val previewImage = findViewById<RelativeLayout>(R.id.previewImage)
        previewImage.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Lấy danh sách các hình ảnh từ Intent
                val imageResource = intent.getStringArrayListExtra("imageResource")

                // Kiểm tra xem danh sách có dữ liệu không
                if (!imageResource.isNullOrEmpty()) {
                    val imageView = findViewById<ImageView>(R.id.imageProduct)

                    // Sử dụng Glide để tải ảnh từ URL vào ImageView
                    Glide.with(this)
                        .load(imageResource[0])  // Lấy ảnh đầu tiên từ danh sách
                        .into(imageView)
                }
            }
            true
        }
        addToCart.setOnClickListener {
            try {
                // Lấy `idProduct` từ Intent
                val productId = intent.getIntExtra("id", 0)
                // Lấy thông tin sản phẩm từ các View
                val productName = findViewById<TextView>(R.id.productName).text.toString()
                val priceText = findViewById<TextView>(R.id.priceTotal).text.toString().replace(" VND", "")
                val description = findViewById<TextView>(R.id.productDes).text.toString()
                val quantityText = findViewById<TextView>(R.id.txtSoLuong).text.toString()

                // Kiểm tra dữ liệu `idProduct` và giá trị đầu vào
                if (productId <= 0) {
                    Toast.makeText(this, "Product ID is invalid", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val unitPrice = priceText.toIntOrNull() ?: 0
                val quantity = quantityText.toIntOrNull() ?: 1

                if (productName.isEmpty() || unitPrice <= 0) {
                    Toast.makeText(this, "Invalid product data", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Tạo đối tượng sản phẩm
                val product = Product(
                    idProduct = productId,
                    name = productName,
                    price = unitPrice,
                    rating = 0f,
                    description = description
                )
                // Thêm kiểm tra sản phẩm đã tồn tại trong giỏ hàng
                val pro_cart = ProductCartDAO(this)
                if (pro_cart.isProductInCart(productId)) {
                    Toast.makeText(this, "Sản phẩm đã tồn tại trong giỏ hàng!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Thêm sản phẩm vào giỏ hàng
                val productCartDAO = ProductCartDAO(this)
                val result = productCartDAO.insertProduct(product, quantity)

                if (result != -1L) {
                    // Hiển thị thông báo thành công
                    Toast.makeText(this, "Thêm thành công!!!", Toast.LENGTH_SHORT).show()
                } else {
                    // Thông báo lỗi khi thêm sản phẩm
                    Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "An error occurred while adding product to cart", Toast.LENGTH_SHORT).show()
            }
        }



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
