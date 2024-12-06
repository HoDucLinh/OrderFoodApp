package com.ltb.orderfoodapp.viewmodel

import android.content.Context
import com.ltb.orderfoodapp.data.dao.ProductCartDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.Restaurant

class ProductViewModel(context: Context) {

    private val productDAO = ProductDAO(context)
    private var products: MutableList<Product> = mutableListOf()


    init {
        fetchProducts()
    }

    // Lấy danh sách sản phẩm từ ProductDAO
    private fun fetchProducts() {
        products = productDAO.getAllProducts()
    }


    fun getProducts(): MutableList<Product> {
        return products
    }
    fun getProductsFilter(kw: String):MutableList<Product>{
        return  products.filter { it.name.contains(kw , ignoreCase = true) }.toMutableList()
    }
    fun  getProductsByRestaurant(restaurant: String): MutableList<Product>{
        return products.filter { it.restaurant == restaurant }.toMutableList()
    }
    fun getProductByCategory(category: String):MutableList<Product>{
        return products.filter { it.category == category }.toMutableList()
    }

    fun close() {
        productDAO.close()
    }
}
