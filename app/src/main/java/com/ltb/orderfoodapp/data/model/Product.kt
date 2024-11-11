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

}

class Food(
    name: String,
    storeName: String,
    price: Int,
    imageResource: String,
    rating: Float,
    category: String,
    description: String
) : Product(
    name = name,
    storeName = storeName,
    price = price,
    imageResource = imageResource,
    rating = rating,
    category = category,
    description = description
)

class Drink(
    name: String,
    storeName: String,
    price: Int,
    imageResource: String,
    rating: Float,
    category: String,
    description: String
) : Product(
    name = name,
    storeName = storeName,
    price = price,
    imageResource = imageResource,
    rating = rating,
    category = category,
    description = description
)


