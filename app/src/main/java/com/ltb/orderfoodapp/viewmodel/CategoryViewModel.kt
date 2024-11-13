package com.ltb.orderfoodapp.viewmodel

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.Restaurant


class CategoryViewModel {
    private val db = FirebaseFirestore.getInstance()
    private var categories: MutableList<Category> = mutableListOf()
    init {
//        val category = listOf(
//            Category(
//                id_category = "CAT001",
//                name = "Food",
//                description = "Các loại đồ ăn"
//            ),
//            Category(
//                id_category = "CAT002",
//                name = "Drink",
//                description = "Các loại nước uống"
//            )
//        )
//        categories.addAll(category)

    }

    // Thêm sản phẩm vào Firebase
    fun addToFirebase() {
        for (category in categories) {
            val categoryId = category.id_category // ID của sản phẩm
            db.collection("categories")
                .document(categoryId)
                .set(category)
                .addOnSuccessListener {
                    Log.d("TAG", "Product added with ID: $categoryId")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding product", e)
                }
        }
    }
    fun fetchData() {
        db.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                categories.clear()
                for (document in result) {
                    val category = document.toObject(Category::class.java)
                    categories.add(category)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ProductViewModel", "Error getting documents.", exception)
            }
    }

    fun getCategories(): List<Category> {
        return categories
    }
}