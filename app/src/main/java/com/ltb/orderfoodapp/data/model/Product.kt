package com.ltb.orderfoodapp.data.model

import android.media.Rating

// Model/Product.kt

data class Product(
    val name: String,
    val storeName: String,
    val price: Int,
    val imageResource: Int,
    val rating: Int
)
