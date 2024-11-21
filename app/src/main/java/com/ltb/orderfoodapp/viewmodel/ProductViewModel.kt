package com.ltb.orderfoodapp.viewmodel

import android.content.Context
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Product

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

    // Trả về danh sách sản phẩm
    fun getProducts(): MutableList<Product> {
        return products
    }
    fun getProductsFilter(kw: String):MutableList<Product>{
        var listFilter : MutableList<Product> = mutableListOf()
        for (product in products){
            if(product.name.contains(kw, ignoreCase = true)){
                listFilter.add(product)
            }
        }
        return listFilter
    }

    fun close() {
        productDAO.close()
    }
}
