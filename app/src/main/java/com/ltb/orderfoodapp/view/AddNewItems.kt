package com.ltb.orderfoodapp.view

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.storage.FirebaseStorage
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.CategoryAdapter
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.databinding.ActivityAddNewItemsBinding
import com.ltb.orderfoodapp.viewmodel.CategoryViewModel
import com.ltb.orderfoodapp.viewmodel.ProductViewModel
import java.io.IOException
import java.util.*

class AddNewItems : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewItemsBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var productDAO: ProductDAO
    private var filePath: Uri? = null
    private var imageStorage: String = ""
    private var selectedCategory: String = "Unknown"
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Storage and DAO
        storage = FirebaseStorage.getInstance()
        productDAO = ProductDAO(this)

        // Set up event listeners
        binding.btnSave.setOnClickListener { handleSave() }
        binding.btnTaiAnh1.setOnClickListener { selectAndUploadImage() }

        // Set up Spinner for categories
        setupSpinner()
    }

    private fun setupSpinner() {
        val viewModel = CategoryViewModel(this)
        val categories = viewModel.getCategoriesName()
        val categoryAdapter = CategoryAdapter(this, categories)
        binding.categorySpinner.adapter = categoryAdapter
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position]
                Log.d("SelectedCategory", "Category selected: $selectedCategory")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedCategory = "Unknown"
            }
        }
    }

    private fun selectAndUploadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.imageView.setImageBitmap(bitmap)
                uploadImage()
            } catch (e: IOException) {
                Log.e("ImageSelection", "Error selecting image: ${e.message}")
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage() {
        val resolvedFilePath = filePath ?: run {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
            return
        }

        val fileDir = "images/${UUID.randomUUID()}.jpg"
        val storageRef = storage.reference.child(fileDir)

        val progressDialog = ProgressDialog(this).apply {
            setMessage("Uploading image...")
            setCancelable(false)
            show()
        }

        storageRef.putFile(resolvedFilePath)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    imageStorage = uri.toString()
                    progressDialog.dismiss()
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun handleSave() {
        if (!validateInput()) return

        val name = binding.nameitem.text.toString()
        val description = binding.description.text.toString()
        val price = binding.txtPrice.text.toString().toFloat()

        val newProduct = Product(
            name = name,
            description = description,
            price = price.toInt(),
            images = listOf(imageStorage).toMutableList(),
            category = selectedCategory
        )

        val isAdded = productDAO.addProduct(newProduct)
        if (isAdded != null) {
            val myFoodList = Intent(this, MyFood ::class.java)
            startActivity(myFoodList)
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(): Boolean {
        val name = binding.nameitem.text.toString()
        val description = binding.description.text.toString()
        val priceText = binding.txtPrice.text.toString()
        val price = priceText.toFloatOrNull()

        return when {
            name.isEmpty() -> {
                Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
                false
            }
            description.isEmpty() -> {
                Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show()
                false
            }
            price == null || price <= 0 -> {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show()
                false
            }
            imageStorage.isEmpty() -> {
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show()
                false
            }
            selectedCategory == "Unknown" -> {
                Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}
