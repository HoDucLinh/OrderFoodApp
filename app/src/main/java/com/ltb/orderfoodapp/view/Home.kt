package com.ltb.orderfoodapp.view

import ProductViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.media.Rating
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.viewmodel.CategoryViewModel

class Home : Activity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var categoriesViewModel : CategoryViewModel
    private lateinit var layoutProduct: LinearLayout
    private lateinit var layoutCategories: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        productViewModel = ProductViewModel()
        categoriesViewModel = CategoryViewModel()
        renderProduct()
        renderCategories()

    }
    @SuppressLint("MissingInflatedId")
    fun  renderProduct(){
        val products = productViewModel.getProducts()
        for (product in products) {
            val itemView = LayoutInflater.from(this).inflate(R.layout.product, null)
            val imgProduct = itemView.findViewById<ImageView>(R.id.img_product)
            val productName = itemView.findViewById<TextView>(R.id.product_name)
            val storeName = itemView.findViewById<TextView>(R.id.store_name)
            val productPrice = itemView.findViewById<TextView>(R.id.product_price)
            val productRating = itemView.findViewById<RatingBar>(R.id.ratingBar)
            layoutProduct = findViewById(R.id.layout_product) as LinearLayout
            imgProduct.setImageResource(product.imageResource)
            productName.text = product.name
            storeName.text = product.storeName
            productPrice.text = product.price.toString()
            productRating.rating = product.rating.toFloat()
            layoutProduct.addView(itemView)
        }
    }
    fun renderCategories(){
        val categories = categoriesViewModel.getCategories()
        for (category in categories) {
            val cateView = LayoutInflater.from(this).inflate(R.layout.categories, null)
            val imgCategories = cateView.findViewById<ImageView>(R.id.img_categories)
            val txtCate = cateView.findViewById<TextView>(R.id.txtCate)
            val txtPrice = cateView.findViewById<TextView>(R.id.txtPrice)
            layoutCategories = findViewById(R.id.layout_categories) as LinearLayout
            imgCategories.setImageResource(category.imageResource)
            txtCate.text = category.name
            txtPrice.text = category.minPrice.toString()
            layoutCategories.addView(cateView)
        }
    }
}