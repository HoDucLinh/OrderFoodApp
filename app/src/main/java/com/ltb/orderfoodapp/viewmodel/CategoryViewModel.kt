package com.ltb.orderfoodapp.viewmodel

import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Category


class CategoryViewModel {

    private val category: MutableList<Category> = mutableListOf()
    private lateinit var cate : Category

    fun getCategories(): List<Category> {
        return category
    }
}