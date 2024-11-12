package com.ltb.orderfoodapp.data.model

open class Product(
    var name: String,
    var storeName: String,
    var price: Int,
    var imageResource: String,
    var rating: Float,
    var category: String,
    var description: String
) {
    constructor() : this("", "", 0, "", 0f, "", "")
}
