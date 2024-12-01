package com.ltb.orderfoodapp.view

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
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

private lateinit var productDAO: ProductDAO
private lateinit var edtName: EditText
private lateinit var edtDescription: EditText
private lateinit var txtPrice: TextView
private lateinit var btnSave: Button
private lateinit var btnUploadImage: ImageButton
private val images: MutableList<String> = mutableListOf()

private val PICK_IMAGE_REQUEST = 1
class AddNewItems : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
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
        val priceText = findViewById<TextView>(R.id.txtPrice).text.toString().replace("$", "")
        val price = priceText.toIntOrNull() ?: 0

        val category = when {
            categoryFood -> "Food"
            categoryDrink -> "Drinks"
            else -> "Unknown"
        }

        Log.d("AddNewItemsActivity", "Name: $nameItem, Description: $description, Price: $price, Category: $category")

        if (nameItem.isEmpty() || price <= 0 || category == "Unknown") {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Chèn dữ liệu vào cơ sở dữ liệu
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("Name", nameItem)
            put("Price", price)
            put("Rating", 0.0)
            put("Description", description)
            put("Category_ID", if (category == "Food") 1 else 2)
            put("Restaurant_ID", 1)
        }

        val newRowId = db.insert("Product", null, values)
        if (newRowId != -1L) {
            Log.d("AddNewItemsActivity", "Product added successfully: ID = $newRowId")
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("AddNewItemsActivity", "Failed to add product")
            Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

}