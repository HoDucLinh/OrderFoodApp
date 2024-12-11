package com.ltb.orderfoodapp.viewmodel

import android.content.Context
import com.ltb.orderfoodapp.data.dao.ProductCartDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Category
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


    fun getProducts(): MutableList<Product> {
        return products
    }

    fun getProductsFilter(kw: String): MutableList<Product> {
        return products.filter { it.getName().contains(kw, ignoreCase = true) }.toMutableList()
    }
    fun getProductByCategory(category: String):MutableList<Product>{
        return products.filter { it.getCategory() == category }.toMutableList()
    }
    fun getProductById(idProduct : Int): MutableList<Product>{
        return products.filter { it.getIdProduct() == idProduct}.toMutableList()
    }
    // Phương thức đóng cơ sở dữ liệu
    fun close() {
        productDAO.close()
    }
    fun getCategories(): List<String> {
        return products.map { it.getCategory() }.distinct()
    }

}
