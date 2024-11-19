package com.ltb.orderfoodapp.view

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
import com.ltb.orderfoodapp.adapter.CategoryAdapter
import com.ltb.orderfoodapp.adapter.ProductAdapter
import com.ltb.orderfoodapp.data.dao.CategoryDAO
import com.ltb.orderfoodapp.viewmodel.CategoryViewModel
import com.ltb.orderfoodapp.viewmodel.ProductViewModel

class Home : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var darkTheme : Switch
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        productViewModel = ProductViewModel(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val nextSearch = findViewById<TextView>(R.id.txtSearch)
        val nextCart = findViewById<ImageButton>(R.id.nextCart)
        val nextMenu = findViewById<ImageButton>(R.id.nextMenu)

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
        productViewModel.close()



    }
    override fun onStart() {
        super.onStart()

        darkTheme = findViewById(R.id.darkTheme)
        setupGridViewProduct()
    setupTheme()
    }
    private fun setupTheme() {
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)

        // Kiểm tra trạng thái theme hiện tại và thay đổi nếu cần
        if (nightMode != darkTheme.isChecked) {
            darkTheme.isChecked = nightMode
        }

        // Thiết lập chế độ tối/sáng
        AppCompatDelegate.setDefaultNightMode(
            if (nightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        darkTheme.setOnCheckedChangeListener { _, isChecked ->
            val newMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            if (AppCompatDelegate.getDefaultNightMode() != newMode) {
                AppCompatDelegate.setDefaultNightMode(newMode)
                val editor = sharedPreferences.edit()
                editor.putBoolean("night", isChecked)
                editor.apply()
            }
        }
    }

    private fun setupGridViewProduct() {
        val products = productViewModel.getProducts()
        val gridView = findViewById<GridView>(R.id.gridviewProduct)
        val adapter = ProductAdapter(this, products)
        gridView.adapter = adapter
    }

}