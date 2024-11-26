package com.ltb.orderfoodapp.data.model

open class Product(
    var idProduct: Int = 0,
    var name: String = "",
    var price: Int = 0,
    var rating: Float = 0f,
    var description: String = "",
    var restaurant: String = "",
    var category: String = "",
    var images: MutableList<String> = mutableListOf(),
)
