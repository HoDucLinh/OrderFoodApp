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
import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.compose.ui.graphics.Color

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


        gridView.setOnItemClickListener { _, _, position, _ ->
            seletedCategory = position
        }
//        gridView.setBackgroundColor(0xFF00FF00.toInt())
        btnDelete.setOnClickListener{
            print(seletedCategory)
            print("123")
            if (seletedCategory != -1) {
                val selectedItem = gridView.adapter.getItem(seletedCategory) as String
                categoryDAO.deleteCategory(selectedItem)
                setupGridViewCategory()

                gridView.setBackgroundColor(0xFF0022D2.toInt())
            }else
            {
                        gridView.setBackgroundColor(0xFF00FF00.toInt())
            }
        }


        btnAdd.setOnClickListener{
            val categoryName = editTextCategoryName.text.toString()
            val categoryDescription = editTextCategoryDescription.text.toString()
            categoryDAO.addCategory(categoryName, categoryDescription)
            setupGridViewCategory()
        }

//        btnDelete.setOnClickListener{
//            val categoryName = editTextCategoryName.text.toString()
//            categoryDAO.deleteCategory(categoryName)
//            setupGridViewCategory()
//        }

    }

    private fun setupGridViewCategory(){
        val categories = categoryViewModel.getCategoriesName()
        val adapter = CategoryAdapter(this,categories)
        gridView.adapter = adapter
    }






}