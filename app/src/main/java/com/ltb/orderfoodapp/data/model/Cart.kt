package com.ltb.orderfoodapp.data.model

class Cart (
    var idCart : Int,
    var totalNumber: Int,
    var userId : Int,
    var products: MutableList<Product>
){
    constructor() :this(0,0, 0, mutableListOf())
}