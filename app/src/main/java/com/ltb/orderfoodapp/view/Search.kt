package com.ltb.orderfoodapp.view

import ProductViewModel
import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.CategoryAdapter
import com.ltb.orderfoodapp.adapter.ProductAdapter
import com.ltb.orderfoodapp.viewmodel.CategoryViewModel

class Search : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        productViewModel = ProductViewModel(this)
        categoryViewModel = CategoryViewModel(this)
        setupGridViewCategory()

        val backHome = findViewById<ImageButton>(R.id.backHome)
        val category = findViewById<GridView>(R.id.gridViewCategoryName)
        val searchbar = findViewById<EditText>(R.id.searchInput)
        val clearInput = findViewById<ImageButton>(R.id.closeBtn)
        val searchInput = findViewById<EditText>(R.id.searchInput)
        searchInput.addTextChangedListener{
            setupGridView(searchInput.text.toString())
        }

        searchbar.requestFocus()
        // Xoa du lieu trong o input
        clearInput.setOnClickListener{
            searchbar.setText("")
            searchbar.requestFocus()
        }
        backHome.setOnClickListener {
            val home = Intent(this, Home::class.java)
            startActivity(home)
        }

        category.setOnItemClickListener { parent, view, position, id ->
            // Lấy đối tượng category tại vị trí người dùng nhấn
            val selectedCategory = parent.getItemAtPosition(position) as String

            // Chuyển qua FoodDetail Activity và truyền dữ liệu
            val categoryIntent = Intent(this, FoodDetail::class.java)
            categoryIntent.putExtra("category", selectedCategory)
            startActivity(categoryIntent)
        }


    }
    private fun setupGridView(kw : String){
        val products = productViewModel.getProductsFilter(kw)
        val gridView = findViewById<GridView>(R.id.gridviewProduct)
        val adapter = ProductAdapter(this, products)
        gridView.adapter = adapter

    }
    private fun setupGridViewCategory(){
        val categories = categoryViewModel.getCategoriesName()
        val recyclerView = findViewById<GridView>(R.id.gridViewCategoryName)
        val adapter = CategoryAdapter(this,categories)
        recyclerView.adapter = adapter
    }
}