package com.ltb.orderfoodapp.viewmodel

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ltb.orderfoodapp.data.model.Image

class ImageViewModel {
    private val db = FirebaseFirestore.getInstance()
    private var images: MutableList<Image> = mutableListOf()

    init {

        // Thêm các đối tượng Image vào danh sách
        images.add(
            Image(
                id_image = "image1",
                value = mutableListOf(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg"
                )
            )
        )

        images.add(
            Image(
                id_image = "image2",
                value = mutableListOf(
                    "https://example.com/image3.jpg",
                    "https://example.com/image4.jpg"
                )
            )
        )

        images.add(
            Image(
                id_image = "image3",
                value = mutableListOf(
                    "https://example.com/image5.jpg",
                    "https://example.com/image6.jpg"
                )
            )
        )
    }

    // Thêm hình ảnh vào Firebase
    fun addToFirebase() {
        for (image in images) {
            val imageId = image.id_image // ID của hình ảnh
            db.collection("images")
                .document(imageId)
                .set(image)
                .addOnSuccessListener {
                    Log.d("TAG", "Image added with ID: $imageId")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding image", e)
                }
        }
    }

    // Lấy dữ liệu hình ảnh từ Firestore
    fun fetchData() {
        db.collection("images")
            .get()
            .addOnSuccessListener { result ->
                images.clear()
                for (document in result) {
                    val image = document.toObject(Image::class.java)
                    images.add(image)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ImageViewModel", "Error getting documents.", exception)
            }
    }

    // Lấy danh sách các hình ảnh
    fun getImages(): MutableList<Image> {
        return images
    }
}