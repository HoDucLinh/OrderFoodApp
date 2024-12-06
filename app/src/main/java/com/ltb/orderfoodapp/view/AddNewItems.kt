package com.ltb.orderfoodapp.view

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Image
import com.ltb.orderfoodapp.data.model.Product
import java.io.IOException
import java.util.UUID
import kotlin.math.log


private lateinit var edtName: EditText
private lateinit var edtDescription: EditText
private lateinit var txtPrice: TextView
private lateinit var btnSave: Button
private lateinit var btnUploadImage: ImageButton
private val images: MutableList<String> = mutableListOf()

private val PICK_IMAGE_REQUEST = 1
class AddNewItems : AppCompatActivity() {
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var productDAO: ProductDAO
    private var imageList : MutableList<String> = mutableListOf()
    private lateinit var imageStorage : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ltb.orderfoodapp.R.layout.activity_add_new_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.ltb.orderfoodapp.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            uploadImage { addProductToDatabase() }
        }
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()
        val addImage = findViewById<ImageButton>(R.id.btnTaiAnh1)
        addImage.setOnClickListener{
            selectAndUpload()
        }
    }

    private fun selectAndUpload() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Image from here..."),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
            } catch (e: IOException) {
//                e.printStackTrace()
            }
        } else {
            Log.e("Image Selection", "No image selected or data is null")
        }
    }

    fun uploadImage(callback: (String?) -> Unit) {
        val resolvedFilePath = filePath ?: run {
            Log.e("Upload Error", "File path is null")
            return
        }
        var fileDir = "images/${resolvedFilePath.lastPathSegment}"
        val riversRef = storage.reference.child(fileDir)
        val uploadTask = riversRef.putFile(resolvedFilePath)
        uploadTask.addOnFailureListener {
            Log.e("Upload Error", "Upload failed: ${it.message}")
        }.addOnSuccessListener {
            Log.d("Upload Success", "File uploaded successfully")
        }
        val storageRef = FirebaseStorage.getInstance().getReference(fileDir)
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            imageStorage = uri.toString()

        }.addOnFailureListener { e ->
            Log.e("Download URL", "Failed to get download URL: ${e.message}")
        }

    }

    private fun addProductToDatabase() {
        val nameItem = findViewById<EditText>(R.id.nameitem).text.toString()
        val description = findViewById<EditText>(R.id.description).text.toString()
        val categoryFood = findViewById<CheckBox>(R.id.cateFood).isChecked
        val categoryDrink = findViewById<CheckBox>(R.id.cateDrink).isChecked
        val priceText = findViewById<EditText>(R.id.txtPrice).text.toString().replace("$", "")

        val price = priceText.toFloatOrNull() ?: 0f


        val category = when {
            categoryFood -> "Food"
            categoryDrink -> "Drinks"
            else -> "Unknown"
        }

        if (nameItem.isEmpty() || description.isEmpty() || price <= 0 || category == "Unknown") {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("AddNewItemsActivity", "Name: $nameItem, Description: $description, Price: $price, Category: $category")
            imageList.add(imageStorage)
        // Tạo sản phẩm mới và thêm vào cơ sở dữ liệu
        val newProduct = Product(name = nameItem,price = price.toInt(), images = imageList, category = category, description = description, restaurant = "Restaurant test")
        productDAO = ProductDAO(this)

        val isAdded = productDAO.addProduct(newProduct)

        Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()

    }


}