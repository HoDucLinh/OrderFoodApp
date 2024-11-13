package com.ltb.orderfoodapp.data.model
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

open class Product(
    var id_product : String,
    var name: String,
    var restaurant_id :DocumentReference,
    var price: Int,
    var rating: Float,
    var category_id: DocumentReference,
    var description: String,
    val image_id: DocumentReference
) {
    constructor() : this("", "",
        FirebaseFirestore.getInstance().collection("restaurants").document("empty"),
        0, 0F,
        FirebaseFirestore.getInstance().collection("categories").document("empty"),
        "",
        FirebaseFirestore.getInstance().collection("images").document("empty"))
}
