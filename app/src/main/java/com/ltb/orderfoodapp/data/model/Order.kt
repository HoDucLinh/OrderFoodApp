package com.ltb.orderfoodapp.data.model

import java.util.Date

class Order(
    private var idOrder: Int = 0,
    private var totalAmount: Float = 0f,
    private var orderStatus: String = "",
    private var orderDate: Date = Date(),
    private var userId: Int = 0,
//    private var restaurantId: Int = 0,
    private var orderDetails: MutableList<OrderDetail> = mutableListOf()
) {
    fun getIdOrder(): Int {
        return idOrder
    }

    fun setIdOrder(value: Int) {
        idOrder = value
    }

    fun getTotalAmount(): Float {
        return totalAmount
    }

    fun setTotalAmount(value: Float) {
        totalAmount = value
    }

    fun getOrderStatus(): String {
        return orderStatus
    }

    fun setOrderStatus(value: String) {
        orderStatus = value
    }

    fun getOrderDate(): Date {
        return orderDate
    }

    fun setOrderDate(value: Date) {
        orderDate = value
    }

    fun getUserId(): Int {
        return userId
    }

    fun setUserId(value: Int) {
        userId = value
    }

//    fun getRestaurantId(): Int {
//        return restaurantId
//    }
//
//    fun setRestaurantId(value: Int) {
//        restaurantId = value
//    }

    fun getOrderDetails(): MutableList<OrderDetail> {
        return orderDetails
    }

    fun setOrderDetails(value: MutableList<OrderDetail>) {
        orderDetails = value
    }
}
