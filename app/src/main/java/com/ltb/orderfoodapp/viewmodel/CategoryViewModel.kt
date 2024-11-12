package com.ltb.orderfoodapp.viewmodel

import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Category


class CategoryViewModel {

    private val category: MutableList<Category> = mutableListOf()
    private lateinit var cate : Category
//    db.collection("category").add(category).addOnSuccessListener { documentReference ->
//        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}") }.addOnFailureListener { e ->
//        Log.w(TAG, "Error adding document", e) }
    init {
        category.add(Category("Áo phông", 200000, R.drawable.burger))
        category.add(Category("Quần jeans" , 300000, R.drawable.burger))
        category.add(Category("Giày thể thao",  500000, R.drawable.burger))
        category.add(Category("Điện thoại", 10000000, R.drawable.burger))
        category.add(Category("Laptop", 20000000, R.drawable.burger))
        category.add(Category("Bàn phím", 500000, R.drawable.burger))
        category.add(Category("Chuột máy tính",  200000, R.drawable.burger))
        category.add(Category("Tai nghe",  300000, R.drawable.burger))
        category.add(Category("Loa di động",  500000, R.drawable.burger))
        category.add(Category("Máy ảnh",  10000000, R.drawable.burger))
    }


    fun getCategories(): List<Category> {
        return category
    }
}