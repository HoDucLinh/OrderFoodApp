package com.ltb.orderfoodapp.data.model

class Restaurant (
    var idRestaurant :Int,
    var nameRestaurant : String,
    var addresses: String,
    var products: MutableList<Product>
){
    constructor():this(0,"", "",mutableListOf())
}