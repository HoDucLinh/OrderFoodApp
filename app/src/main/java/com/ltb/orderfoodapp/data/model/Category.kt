package com.ltb.orderfoodapp.data.model

// Category
data class Category(
        var idCategory: Int = 0,
        var name: String = "",
        var description: String = "",
        var product : MutableList<Product> = mutableListOf()
) {

}
