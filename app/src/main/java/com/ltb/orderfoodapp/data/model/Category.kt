package com.ltb.orderfoodapp.data.model

// Category
data class Category(
        var idCategory: Int,
        var name: String,
        var description: String,
) {
        constructor() : this(0, "", "")
}
