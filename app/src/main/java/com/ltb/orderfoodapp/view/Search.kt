package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.CategoryAdapter
import com.ltb.orderfoodapp.adapter.ProductAdapter
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.viewmodel.CategoryViewModel
import com.ltb.orderfoodapp.viewmodel.ProductViewModel

class Search : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var products : MutableList<Product>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        productViewModel = ProductViewModel(this)
        categoryViewModel = CategoryViewModel(this)
        products = productViewModel.getProducts()
        setupGridViewCategory()
        setupGridView()
        val backHome = findViewById<ImageButton>(R.id.backHome)
        val category = findViewById<GridView>(R.id.gridViewCategoryName)
        val searchbar = findViewById<EditText>(R.id.searchInput)
        val clearInput = findViewById<ImageButton>(R.id.closeBtn)
        val searchInput = findViewById<EditText>(R.id.searchInput)
        val filter = findViewById<ImageButton>(R.id.searchBtn)
        searchInput.addTextChangedListener{

            products = productViewModel.getProductsFilter(searchInput.text.toString())
            setupGridView()
        }
        filter.setOnClickListener{

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
            val selectedCategory = parent.getItemAtPosition(position) as String
            products = productViewModel.getProductByCategory(selectedCategory)
            setupGridView()

        }


    }


    private fun setupGridView(){
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