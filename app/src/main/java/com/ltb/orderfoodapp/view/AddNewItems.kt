package com.ltb.orderfoodapp.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Product

private lateinit var productDAO: ProductDAO
private lateinit var edtName: EditText
private lateinit var edtDescription: EditText
private lateinit var txtPrice: TextView
private lateinit var btnSave: Button
private lateinit var btnUploadImage: ImageButton
private val images: MutableList<String> = mutableListOf()

private val PICK_IMAGE_REQUEST = 1
class AddNewItems : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ltb.orderfoodapp.R.layout.activity_add_new_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.ltb.orderfoodapp.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Khởi tạo DAO
        productDAO = ProductDAO(this)

        // Ánh xạ các view
        edtName = findViewById(R.id.nameitem)
        edtDescription = findViewById(R.id.description)
        txtPrice = findViewById(R.id.txtPrice)
        btnSave = findViewById(R.id.btnSave)
        btnUploadImage = findViewById(R.id.imageButton11)

        // Sự kiện chọn ảnh
        btnUploadImage.setOnClickListener {
            openImagePicker()
        }

        // Sự kiện nút lưu
        btnSave.setOnClickListener {
            saveProduct()
        }
    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Cho phép chọn nhiều ảnh
        }
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                // Người dùng chọn nhiều ảnh
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    images.add(imageUri.toString())
                }
            } else if (data?.data != null) {
                // Người dùng chọn một ảnh
                val imageUri: Uri = data.data!!
                images.add(imageUri.toString())
            }
            Toast.makeText(this, "${images.size} images selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProduct() {
        val name = edtName.text.toString().trim()
        val description = edtDescription.text.toString().trim()
        val price = txtPrice.text.toString().replace("$", "").toIntOrNull() ?: 0

        if (name.isEmpty() || description.isEmpty() || price <= 0) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val product = Product(
            name = name,
            price = price,
            rating = 0f, // Giá trị mặc định
            description = description,
            restaurant = "Default Restaurant", // Thay thế bằng giá trị người dùng chọn
            category = "Default Category",    // Thay thế bằng giá trị người dùng chọn
            images = images
        )

        try {
            val newProductId = productDAO.addProduct(product)
            Toast.makeText(this, "Product added successfully with ID: $newProductId", Toast.LENGTH_SHORT).show()
            finish() // Quay lại màn hình trước đó
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to add product: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        productDAO.close()
    }
}