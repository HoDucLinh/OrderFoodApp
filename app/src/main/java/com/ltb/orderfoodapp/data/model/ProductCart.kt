package com.ltb.orderfoodapp.data.model

class ProductCart(
    var idProductCart : Int = 0,
    var productId  : Int,
    var cartId : Int,
    var quantity : Int,
    var name: String,
    var price: Int,
    var rating: Float = 0f,
    var images: MutableList<String> = mutableListOf(),
)