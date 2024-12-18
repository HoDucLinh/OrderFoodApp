package com.ltb.orderfoodapp.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.CategoryAdapter
import com.ltb.orderfoodapp.data.dao.CategoryDAO
import com.ltb.orderfoodapp.viewmodel.CategoryViewModel
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract.Colors
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.ltb.orderfoodapp.data.ConfirmDialog

class AddNewCategory : AppCompatActivity() {
    private lateinit var gridView: GridView
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryDAO : CategoryDAO

    private var seletedCategory: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_category)
        categoryViewModel = CategoryViewModel(this)
        categoryDAO= CategoryDAO(this)
        gridView = findViewById<GridView>(R.id.gridViewCategoryName1)
        setupGridViewCategory()

        val editTextCategoryName = findViewById<EditText>(R.id.editTextCategoryName)
        val editTextCategoryDescription = findViewById<EditText>(R.id.editTextCategoryDescription)
        val btnAdd = findViewById<Button>(R.id.btn_add_category)
        val btnDelete = findViewById<Button>(R.id.btn_deletecategory)
        val close = findViewById<ImageButton>(R.id.close)
        val reset = findViewById<Button>(R.id.btnReset)

        reset.setOnClickListener {
            editTextCategoryName.text.clear()
            editTextCategoryDescription.text.clear()
        }
        close.setOnClickListener {
            val menu = Intent(this, Menu::class.java)
            startActivity(menu)
        }


        gridView.setOnItemClickListener { _, _, position, _ ->
            seletedCategory = position
            val adapter = gridView.adapter as CategoryAdapter
            adapter.selectedPosition = position
            adapter.notifyDataSetChanged()
        }

        btnDelete.setOnClickListener {
            if (seletedCategory != -1 && gridView.adapter.count > 0) {
                val selectedItem = gridView.adapter.getItem(seletedCategory) as String

                //
                val confirmDialog = ConfirmDialog()
                confirmDialog.showDeleteConfirmationDialog( this){
                    categoryDAO.deleteCategory(selectedItem)
                    Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }

                seletedCategory = -1 // Reset vị trí được chọn
                setupGridViewCategory()
            } else {
                // Hiển thị thông báo nếu không có danh mục hoặc chưa chọn danh mục
                Toast.makeText(this, "Không có danh mục nào để xóa!", Toast.LENGTH_SHORT).show()
            }
        }




        btnAdd.setOnClickListener {
            val categoryName = editTextCategoryName.text.toString().trim()
            val categoryDescription = editTextCategoryDescription.text.toString().trim()

            if (categoryName.isEmpty()) {
                editTextCategoryName.error = "Tên danh mục không được để trống"
                return@setOnClickListener
            }
            if (categoryDescription.isEmpty()) {
                editTextCategoryDescription.error = "Mô tả không được để trống"
                return@setOnClickListener
            }

            val result = categoryDAO.addCategory(categoryName, categoryDescription)
            if (result == -1L) {
                Log.e("AddNewCategory", "Danh mục đã tồn tại.")
            } else {
                setupGridViewCategory()
            }
            editTextCategoryName.text.clear()
            editTextCategoryDescription.text.clear()
        }


    }

    private fun setupGridViewCategory() {
        val categories = categoryViewModel.getCategoriesName()
        if (categories.isEmpty()) {
            Toast.makeText(this, "Không có danh mục nào.", Toast.LENGTH_SHORT).show()
        }
        val adapter = CategoryAdapter(this, categories)
        gridView.adapter = adapter
    }







}