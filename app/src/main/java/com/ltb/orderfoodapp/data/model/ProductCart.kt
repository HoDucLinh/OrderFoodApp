package com.ltb.orderfoodapp.data.model

class ProductCart(
    var idProductCart : Int,
    var productId  : Int,
    var cartId : Int,
    var quantity : Int
){
    constructor() : this (0,0,0,0)
}