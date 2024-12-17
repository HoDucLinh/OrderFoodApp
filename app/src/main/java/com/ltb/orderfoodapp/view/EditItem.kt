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
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.CategoryAdapter
import com.ltb.orderfoodapp.data.dao.ImageDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.viewmodel.CategoryViewModel
import java.io.IOException
import java.util.*

class EditItem : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var productDAO: ProductDAO
    private var filePath: Uri? = null
    private var imageStorage: String = ""
    private var selectedCategory: String = "Unknown"
    private val PICK_IMAGE_REQUEST = 1
    private var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        // Khởi tạo Firebase và DAO
        storage = FirebaseStorage.getInstance()
        productDAO = ProductDAO(this)

        // Lấy productId từ Intent
        productId = intent.getIntExtra("product_id", -1)
        if (productId == -1) {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show()
            finish()
        }

        val product = productDAO.getProductById(productId)
        val imageUrls = ImageDAO(this).getImagesByProductId(productId)
        println("Image ${imageStorage}")


        findViewById<EditText>(R.id.editTextName).setText(product.getName())
        findViewById<EditText>(R.id.editPrice).setText(product.getPrice().toString())
        findViewById<EditText>(R.id.description).setText(product.getDescription())
        imageStorage = imageUrls.firstOrNull()?.getValue() ?: ""
        val imageView = findViewById<ImageView>(R.id.editImage)
        Glide.with(this).load(imageStorage).into(imageView)

        setupSpinner(product.getCategory())

        findViewById<ImageButton>(R.id.btnTaiAnh1).setOnClickListener { selectAndUploadImage() }

        findViewById<Button>(R.id.btnSave).setOnClickListener { handleSave(product) }
    }

    private fun setupSpinner(selectedCategoryName: String) {
        val viewModel = CategoryViewModel(this)
        val categories = viewModel.getCategoriesName()
        val categoryAdapter = CategoryAdapter(this, categories)
        val spinner = findViewById<Spinner>(R.id.categorySpinner)

        spinner.adapter = categoryAdapter
        spinner.setSelection(categories.indexOf(selectedCategoryName))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position]
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
                findViewById<ImageView>(R.id.editImage).setImageBitmap(bitmap)
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

    private fun handleSave(product: Product) {
        val name = findViewById<EditText>(R.id.editTextName).text.toString()
        val description = findViewById<EditText>(R.id.description).text.toString()
        val priceText = findViewById<EditText>(R.id.editPrice).text.toString()
        val price = priceText.toFloatOrNull()

        if (name.isEmpty() || description.isEmpty() || price == null || price <= 0 || imageStorage.isEmpty() || selectedCategory == "Unknown") {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedProduct = product.copy(
            name = name,
            description = description,
            price = price.toInt(),
            images = mutableListOf(imageStorage),
        )
        val categoryId = ProductDAO(this).getOrInsertCategory(selectedCategory)
        val isUpdated = productDAO.updateProduct(updatedProduct)
        ProductDAO(this).updateProductCategory(productId.toLong(), categoryId)
        if (isUpdated) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show()
        }
    }
}
