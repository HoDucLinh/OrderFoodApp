package com.ltb.orderfoodapp.data.model

class OrderDetail (
    var idOrderDetail : Int,
    var orderId : Int,
    var productId : Int,
    var quantity : Int,
    var unitPrice : Float,
    var totalPrice : Float
){
    constructor() : this(0, 0,0,0,0f,0f)
}