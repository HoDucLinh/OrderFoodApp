package com.ltb.orderfoodapp.viewmodel

import android.content.Context
import com.ltb.orderfoodapp.data.dao.ProductCartDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart

class ProductCartViewModel (context: Context) {
    private val productCartDAO = ProductCartDAO(context)
    private var productCart: MutableList<ProductCart> = mutableListOf()
    private var cartTotal : Int = 0

    init {
        fetchProductCart()
    }
    fun getProductCartByCartID(cartId : Int): MutableList<ProductCart> {
        val productByID = productCart.filter { it.getCartId() == cartId}.toMutableList()
        cartTotal = productByID.size
        return productByID
    }

    private fun fetchProductCart(){
        productCart = productCartDAO.getAllProductsOfCart()
    }
    fun getProduct(): MutableList<ProductCart> {
        return productCart
    }
    fun getProductDone() : MutableList<ProductCart>{
        return productCart

    }
    fun getCartTotal() : Int{
        return cartTotal
    }
}