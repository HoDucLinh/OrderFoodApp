package com.ltb.orderfoodapp.view

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
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


private lateinit var edtName: EditText
private lateinit var edtDescription: EditText
private lateinit var txtPrice: TextView
private lateinit var btnSave: Button
private lateinit var btnUploadImage: ImageButton
private val images: MutableList<String> = mutableListOf()

private val PICK_IMAGE_REQUEST = 1
class AddNewItems : AppCompatActivity() {
    private lateinit var productDAO: ProductDAO
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
            addProductToDatabase()
        }

    }
    private fun addProductToDatabase() {
        val nameItem = findViewById<EditText>(R.id.nameitem).text.toString()
        val description = findViewById<EditText>(R.id.description).text.toString()
        val categoryFood = findViewById<CheckBox>(R.id.cateFood).isChecked
        val categoryDrink = findViewById<CheckBox>(R.id.cateDrink).isChecked
        val priceText = findViewById<EditText>(R.id.txtPrice).text.toString().replace("$", "")

        // Chuyển giá trị thành Float thay vì Int để xử lý các giá trị thập phân
        val price = priceText.toFloatOrNull() ?: 0f

        val category = when {
            categoryFood -> "Food"
            categoryDrink -> "Drinks"
            else -> "Unknown"
        }

        // Kiểm tra trường hợp các trường quan trọng trống
        if (nameItem.isEmpty() || description.isEmpty() || price <= 0 || category == "Unknown") {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("AddNewItemsActivity", "Name: $nameItem, Description: $description, Price: $price, Category: $category")

        // Tạo sản phẩm mới và thêm vào cơ sở dữ liệu
        val newProduct = Product(name = nameItem,price = price.toInt(), category = category, description = description)
        productDAO = ProductDAO(this)

        val isAdded = productDAO.addProduct(newProduct)

        Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()

    }


}