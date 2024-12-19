package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.data.dao.RatingDAO
import com.ltb.orderfoodapp.viewmodel.OrderViewModel
import com.ltb.orderfoodapp.viewmodel.ReviewViewModel

class SellerDashboardHome : AppCompatActivity() {
    private lateinit var orderViewModel: OrderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seller_dashboard_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val menu = findViewById<ImageButton>(R.id.nextMenu)
        val orders = findViewById<TextView>(R.id.totalOrders)
        val runningOrders = findViewById<TextView>(R.id.runningOrders)
        val revenue = findViewById<TextView>(R.id.totalRevenue)
        val totalRating = findViewById<TextView>(R.id.totalRating)
        val totalReview = findViewById<TextView>(R.id.totalReview)
        val allReview = findViewById<TextView>(R.id.allReview)
        totalReview.text = "Total ${ReviewViewModel(this).reviewCount()} review"
        totalRating.text = RatingDAO(this).getAverageRatingForAllProducts().toString()

        allReview.setOnClickListener {
            val allReview = Intent(this,ReviewScreen:: class.java)
            startActivity(allReview)
        }
        menu.setOnClickListener{
            val next_menu = Intent(this , Menu::class.java)
            startActivity(next_menu)
        }
        orderViewModel = OrderViewModel(this)
        orders.text = orderViewModel.getTotalOrders().toString()
        runningOrders.text = orderViewModel.getRunningOrdersCount().toString()
        revenue.text = orderViewModel.getRevenue().toString()





        val scrollLayout = findViewById<LinearLayout>(R.id.scrollLayout)
        val orderDAO = OrderDAO(this)
        val productList = orderDAO.getTopSellingProducts()
        for (product in productList) {

            val productView = LayoutInflater.from(this).inflate(R.layout.product_search, scrollLayout, false)
            val imgProduct = productView.findViewById<ImageView>(R.id.img_product)
            val productNameTextView = productView.findViewById<TextView>(R.id.product_name)
            val productPriceTextView = productView.findViewById<TextView>(R.id.product_price)
            val ratingTextView = productView.findViewById<TextView>(R.id.ratingBar)

            productNameTextView.text = product.getName()
            productPriceTextView.text = "${product.getPrice()}VND"
            ratingTextView.text = product.getRating().toString()
            if (product.getImages().isNotEmpty() && product.getImages()[0] != null) {
                Glide.with(this)
                    .load(product.getImages()[0])
                    .error(R.drawable.cancel)
                    .into(imgProduct)
            } else {
                Glide.with(this)
                    .load(R.drawable.burger)
                    .into(imgProduct)
            }
            scrollLayout.addView(productView)
        }


    }
}