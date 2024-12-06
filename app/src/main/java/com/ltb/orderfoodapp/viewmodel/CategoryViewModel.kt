package com.ltb.orderfoodapp.viewmodel

import android.content.Context
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.CategoryDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Product


class CategoryViewModel(context: Context) {

    private val categoryDAO = CategoryDAO(context)
    private var products: MutableList<Product> = mutableListOf()
    private var categories : MutableList<Category> = mutableListOf()


    private fun getProducts(categoryName : String) {
        products = categoryDAO.getProductByCategoryName(categoryName)
    }
     fun getCategoriesName() : MutableList<String> {
        categories = categoryDAO.getAllCategories()
        val categoryName : MutableList<String> = mutableListOf()
            for (c in categories) {
                categoryName.add(c.name)
        }
        return categoryName

    }


    fun getProducts(): MutableList<Product> {
        return products
    }
}
