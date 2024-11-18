package com.ltb.orderfoodapp.data.model

import java.util.Date

class Order (
    var idOrder : Int,
    var totalAmount : Float,
    var orderStatus : String,
    var orderDate : Date,
    var userId : Int ,
    var restaurantId : Int,
    var orderDetails: MutableList<OrderDetail>
)
{
    constructor() : this(0,0f, "" , Date(),0,0, mutableListOf())
}