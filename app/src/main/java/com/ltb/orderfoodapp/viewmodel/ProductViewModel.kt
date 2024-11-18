package com.ltb.orderfoodapp.viewmodel

import android.content.Context
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Product

class ProductViewModel(context: Context) {

    private val productDAO = ProductDAO(context)
    private var products: MutableList<Product> = mutableListOf()

    init {
        productDAO.open()
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

    // Đảm bảo đóng kết nối khi ViewModel bị hủy
    fun close() {
        productDAO.close()
    }
}
