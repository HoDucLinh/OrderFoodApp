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

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        productViewModel = ProductViewModel()
        val nextSearch = findViewById<TextView>(R.id.txtSearch)
        val nextCart = findViewById<ImageButton>(R.id.nextCart)
        val nextMenu = findViewById<ImageButton>(R.id.nextMenu)

//        render product
        setupGridView()
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
        val darkTheme = findViewById<Switch>(R.id.darkTheme)
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night",false)

        if (nightMode){
            darkTheme.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }


        darkTheme.setOnCheckedChangeListener { _, isChecked ->

            if (!isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()

            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night",true)
                editor.apply()
            }
        }

    }

    private fun setupGridView() {
        val products = productViewModel.getProducts()
        val gridView = findViewById<GridView>(R.id.gridviewProduct)
        val adapter = ProductAdapter(this, products)
        gridView.adapter = adapter

    }
}