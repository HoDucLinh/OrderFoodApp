package com.ltb.orderfoodapp.data.model

class OrderDetail(
    private var idOrderDetail: Int = 0,
    private var orderId: Int = 0,
    private var productId: Int = 0,
    private var quantity: Int = 0,
    private var unitPrice: Float = 0f,
    private var totalPrice: Float = 0f
) {

    fun getIdOrderDetail(): Int {
        return idOrderDetail
    }

    fun setIdOrderDetail(value: Int) {
        idOrderDetail = value
    }


    fun getOrderId(): Int {
        return orderId
    }

    fun setOrderId(value: Int) {
        orderId = value
    }


    fun getProductId(): Int {
        return productId
    }

    fun setProductId(value: Int) {
        productId = value
    }


    fun getQuantity(): Int {
        return quantity
    }

    fun setQuantity(value: Int) {
        quantity = value
    }


    fun getUnitPrice(): Float {
        return unitPrice
    }

    fun setUnitPrice(value: Float) {
        unitPrice = value
    }

   fun getTotalPrice(): Float {
        return totalPrice
    }

    fun setTotalPrice(value: Float) {
        totalPrice = value
    }
}
