package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.CategoryAdapter
import com.ltb.orderfoodapp.adapter.ItemAdapter
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.viewmodel.CategoryViewModel
import com.ltb.orderfoodapp.viewmodel.ProductViewModel

class MyFood : AppCompatActivity() {
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var productList: MutableList<Product>
    private lateinit var filteredList: MutableList<Product>
    private lateinit var listView: ListView
    private lateinit var categorySpinner: Spinner
    private lateinit var totalItemsTextView : TextView
    private lateinit var viewModel : CategoryViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_food)
        enableEdgeToEdge()
//        viewModel = ProductViewModel(this)
//        productList = viewModel.getProducts().toMutableList()
//        filteredList = productList

        listView = findViewById(R.id.listitem)
        categorySpinner = findViewById(R.id.categorySpinner)
        totalItemsTextView = findViewById(R.id.textView6)

        setupCategorySpinner()

        // Nút quay lại
        findViewById<View>(R.id.back).setOnClickListener {
            startActivity(Intent(this, Menu::class.java))
        }

        // Nút thêm mới
        findViewById<View>(R.id.btnAddNew).setOnClickListener {
            startActivity(Intent(this, AddNewItems::class.java))
        }
    }
    private fun setupCategorySpinner() {
        productList = ProductViewModel(this).getProducts()
        productList.forEach {  p -> println(p.getName()) }
        filteredList = productList
        val categories = mutableListOf("All")
        categories.addAll(CategoryViewModel(this).getCategoriesName())
        val categoryAdapter = CategoryAdapter(this, categories)
        categorySpinner.adapter = categoryAdapter

        // Xử lý khi chọn danh mục
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                filteredList = if (selectedCategory == "All") {
                    productList
                } else {
                    productList.filter { it.getCategory() == selectedCategory }.toMutableList()
                }
                itemAdapter = ItemAdapter(this@MyFood, filteredList)
                listView.adapter = itemAdapter
                totalItemsTextView.text = "Total ${filteredList.size} items"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    override fun onStart() {
        super.onStart()
        setupCategorySpinner()
        setUpList()
    }

    override fun onResume() {
        super.onResume()
        setupCategorySpinner()
        setUpList()
    }

    fun setUpList(){
        val viewModel = ProductViewModel(this)
        productList = viewModel.getProducts().toMutableList()
        filteredList = productList
        itemAdapter = ItemAdapter(this, filteredList)
        listView.adapter = itemAdapter
        totalItemsTextView.text = "Total ${filteredList.size} items"
    }

}
