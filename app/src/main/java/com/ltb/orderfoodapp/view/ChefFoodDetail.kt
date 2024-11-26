package com.ltb.orderfoodapp.view

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.DatabaseHelper

class ChefFoodDetail : AppCompatActivity() {
    private lateinit var descriptionEditText: EditText
    private lateinit var nameTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var editButton: Button
    private var isEditing = false // Trạng thái chỉnh sửa
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chef_food_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Ánh xạ view
        descriptionEditText = findViewById(R.id.description)
        nameTextView = findViewById(R.id.restaurantName)
        priceTextView = findViewById(R.id.textView19)
        addressTextView = findViewById(R.id.restaurantAddress)
        ratingTextView = findViewById(R.id.txtrating)
        editButton = findViewById(R.id.btnEdit)

        // Ban đầu `EditText` không chỉnh sửa được
        descriptionEditText.isEnabled = false

        // Sự kiện nhấn nút Edit
        editButton.setOnClickListener {
            if (isEditing) {
                // Lưu thông tin
                saveItemToDatabase()
                toggleEditing(false) // Chuyển về trạng thái chỉ xem
            } else {
                // Chuyển sang chế độ chỉnh sửa
                toggleEditing(true)
            }
        }
    }
    // Hàm bật/tắt chế độ chỉnh sửa
    private fun toggleEditing(editing: Boolean) {
        isEditing = editing
        editButton.text = if (editing) "Save" else "Edit"
        descriptionEditText.isEnabled = editing
        if (editing) {
            descriptionEditText.requestFocus()
        }
    }

    // Hàm lưu thông tin vào database
    private fun saveItemToDatabase() {
        val updatedDescription = descriptionEditText.text.toString().trim()

        if (updatedDescription.isEmpty()) {
            Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Giả sử bạn sử dụng SQLite
        val db = DatabaseHelper(this).writableDatabase

        val values = ContentValues().apply {
            put("description", updatedDescription) // Tên cột trong database
        }

        val rows = db.update("Items", values, "id = ?", arrayOf("1")) // Cập nhật item có id cụ thể
        if (rows > 0) {
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }
}