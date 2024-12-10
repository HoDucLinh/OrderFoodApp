package com.ltb.orderfoodapp.viewmodel

import android.content.Context
import com.ltb.orderfoodapp.data.dao.ProductCartDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart

class ProductCartViewModel (context: Context) {
    private val productCartDAO = ProductCartDAO(context)
    private var productCart: MutableList<ProductCart> = mutableListOf()

    init {
        fetchProductCart()
    }
    fun getProductCartByCartID(cartId : Int): MutableList<ProductCart> {
        return productCart.filter { it.getCartId() == cartId}.toMutableList()
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

    fun getProductByCartId(cardId: Int): Int{
        println(cardId)
        val a =  productCart.filter { it.cartId == cardId }
        for (x in a){
            println(x.name)
        }
        println( " a "  + a )
        return a.size
    }
}