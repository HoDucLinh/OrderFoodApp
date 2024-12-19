package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ReviewAdapter
import com.ltb.orderfoodapp.data.dao.ProductCartDAO
import com.ltb.orderfoodapp.data.dao.RatingDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.User

class FoodDetail : AppCompatActivity() {

    private lateinit var ratingDAO: RatingDAO

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
//        val previewImage = findViewById<RelativeLayout>(R.id.previewImage)
        ratingDAO = RatingDAO(this)
//        previewImage.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_UP) {
//                val imageResource = intent.getStringArrayListExtra("imageResource")
//
//                // Kiểm tra xem danh sách có dữ liệu không
//                if (!imageResource.isNullOrEmpty()) {
//                    val imageView = findViewById<ImageView>(R.id.imageProduct)
//
//                    Glide.with(this)
//                        .load(imageResource[0])  // Lấy ảnh đầu tiên từ danh sách
//                        .into(imageView)
//                }
//            }
//            true
//        }
        addToCart.setOnClickListener {
            val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
            if(isLoggedIn){
                try {
                    val productId = intent.getIntExtra("id", 0)

                    val productName = findViewById<TextView>(R.id.productName).text.toString()
                    val priceText = findViewById<TextView>(R.id.priceTotal).text.toString().replace(" VND", "")
                    val description = findViewById<TextView>(R.id.productDes).text.toString()
                    val quantityText = findViewById<TextView>(R.id.txtSoLuong).text.toString()

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

                    // Lấy thông tin người dùng và giỏ hàng
                    val user = sharedPreferences.getString("user", "")
                    val userObject = Gson().fromJson(user, User::class.java)
                    val cartId = userObject.getCartId()

                    // Kiểm tra sản phẩm đã tồn tại trong giỏ hàng của người dùng
                    val productCartDAO = ProductCartDAO(this)
                    if (productCartDAO.isProductInCart(productId, cartId)) {
                        Toast.makeText(this, "Sản phẩm đã tồn tại trong giỏ hàng!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // Thêm sản phẩm vào giỏ hàng
                    val result = productCartDAO.insertProduct(product, quantity, cartId)
                    if (result != -1L) {
                        Toast.makeText(this, "Thêm thành công!!!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "An error occurred while adding product to cart", Toast.LENGTH_SHORT).show()
                }
            }else
            {
                Toast.makeText(this, "Please login to buy", Toast.LENGTH_SHORT).show()
                    val signIn = Intent(this,SignIn::class.java)
                    startActivity(signIn)
                    finish()
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

        setGridViewReview()
    }

    fun getProductInfor() {
        val name = intent.getStringExtra("name") ?: ""
//        val storeName = intent.getStringExtra("storeName") ?: ""
        val price = intent.getIntExtra("price", 0)
        val imageResource = intent.getStringArrayListExtra("imageResource") ?: arrayListOf()
        val rating = intent.getFloatExtra("rating", 0f)
        val category = intent.getStringExtra("category") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val productNameTextView = findViewById<TextView>(R.id.productName)
//        val storeNameTextView = findViewById<TextView>(R.id.restaurantName)
        val priceTextView = findViewById<TextView>(R.id.priceTotal)
        val ratingTextView = findViewById<TextView>(R.id.productRating)
        val descriptionTextView = findViewById<TextView>(R.id.productDes)
        val imageView = findViewById<ImageView>(R.id.imageProduct)

        productNameTextView.text = name
//        storeNameTextView.text = "storeName"
        priceTextView.text = "${price}"
        ratingTextView.text = "$rating"
        descriptionTextView.text = description
        Glide.with(this).load(imageResource?.getOrNull(0) ?: R.drawable.burger).into(imageView)
    }

    fun setGridViewReview() {
        val productId = intent.getIntExtra("id", 0)
        val reviews = ratingDAO.getReviewsForProduct(productId)
        val gridView = findViewById<GridView>(R.id.gridview_review)
        val adapter = ReviewAdapter(this, reviews)
        gridView.adapter = adapter
    }
}
