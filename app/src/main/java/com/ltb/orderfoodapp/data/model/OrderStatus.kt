package com.ltb.orderfoodapp.data.model

class OrderStatus (
    var idOrderStatus : Int,
    var orderId : Int,
    var statusId : Int
){
    constructor() : this(0,0,0)
}