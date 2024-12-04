package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.ltb.orderfoodapp.data.model.Image

class ImageDAO(private val db: SQLiteDatabase) {
    lateinit var storage: FirebaseStorage

    // Lấy danh sách hình ảnh theo Product_ID
    fun getImagesByProductId(productId: Int): List<Image> {
        val cursor = db.query(
            "Image",
            arrayOf("ID", "Value", "Product_ID"),
            "Product_ID = ?",
            arrayOf(productId.toString()),
            null, null, null
        )
        val images = mutableListOf<Image>()
        cursor.use {
            while (it?.moveToNext() == true) {
                images.add(
                    Image(
                        idImage = it.getInt(it.getColumnIndexOrThrow("ID")),
                        value = it.getString(it.getColumnIndexOrThrow("Value")),
                        productId = it.getInt(it.getColumnIndexOrThrow("Product_ID"))
                    )
                )
            }
        }
        return images
    }

    // Thêm hình ảnh vào bảng Image
    fun addImage(image : String, productId: Int): Long {
        val values = ContentValues().apply {
            put("Value", image)
            put("Product_ID", productId)
        }
        return db.insert("Image", null, values)
    }
    fun addImageToFirebase(imagePath: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpeg") // Change based on your file type
            .build()

        val uploadTask = storageRef.child("images/${imagePath.lastPathSegment}").putFile(imagePath, metadata)

        uploadTask
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                Log.d("FirebaseUpload", "Upload is $progress% done")
            }
            .addOnPausedListener {
                Log.d("FirebaseUpload", "Upload is paused")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Upload failed: ${exception.message}")
            }
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("FirebaseUpload", "Upload successful. File available at $uri")
                }
            }
    }

}
