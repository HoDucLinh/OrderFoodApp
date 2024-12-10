package com.ltb.orderfoodapp.data.model

class Cart (
    private var idCart : Int = 0,
    private var totalNumber: Int = 0,
    private var userId : Int = 0,
    private var products: MutableList<Product> = mutableListOf()
){
    fun getIdCart(): Int {
        return idCart
    }

    fun setIdCart(value: Int) {
        idCart = value
    }

    fun getTotalNumber(): Int {
        return totalNumber
    }

    fun setTotalNumber(value: Int) {
        totalNumber = value
    }

    fun getUserId(): Int {
        return userId
    }

    fun setUserId(value: Int) {
        userId = value
    }

    fun getProducts(): MutableList<Product> {
        return products
    }

    fun setProducts(value: MutableList<Product>) {
        products = value
    }

}