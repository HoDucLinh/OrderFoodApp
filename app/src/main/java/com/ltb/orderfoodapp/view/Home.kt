package com.ltb.orderfoodapp.view

import ProductViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ProductAdapter

class Home : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var darkTheme : Switch
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        productViewModel = ProductViewModel()
        productViewModel.fetchData()
        val nextSearch = findViewById<TextView>(R.id.txtSearch)
        val nextCart = findViewById<ImageButton>(R.id.nextCart)
        val nextMenu = findViewById<ImageButton>(R.id.nextMenu)

//        render product

//        chuyen sang trang tim kiem
        nextSearch.setOnClickListener {
            val nextSearch = Intent(this, Search::class.java)
            startActivity(nextSearch)
        }
//        chuyen sang cart
        nextCart.setOnClickListener {
            val nextCart = Intent(this, MyCart::class.java)
            startActivity(nextCart)
        }
        // Chuyen sang menu
        nextMenu.setOnClickListener {
            val nextMenu = Intent(this, MyMainMenu::class.java)
            startActivity(nextMenu)
        }



    }
    override fun onStart() {
        super.onStart()
        darkTheme = findViewById<Switch>(R.id.darkTheme)
        // fetch data tu firebase
        productViewModel.fetchData()
        setupGridView()
//        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val nightMode = sharedPreferences.getBoolean("night", false)
//
//        if (nightMode != darkTheme.isChecked) {
//            darkTheme.isChecked = nightMode
//        }
//
//        AppCompatDelegate.setDefaultNightMode(
//            if (nightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
//        )
//
//        darkTheme.isChecked = nightMode // Thiết lập lại trạng thái của Switch
//        darkTheme.setOnCheckedChangeListener { _, isChecked ->
//            val newMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
//
//            // Kiểm tra nếu chế độ hiện tại khác với chế độ mong muốn
//            if (AppCompatDelegate.getDefaultNightMode() != newMode) {
//                AppCompatDelegate.setDefaultNightMode(newMode)
//                editor.putBoolean("night", isChecked)
//                editor.apply()
//            }
//        }

    }

    private fun setupGridView() {
        val products = productViewModel.getProducts()
        val gridView = findViewById<GridView>(R.id.gridviewProduct)
        val adapter = ProductAdapter(this, products)
        gridView.adapter = adapter

    }
}