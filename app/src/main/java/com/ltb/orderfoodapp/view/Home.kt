package com.ltb.orderfoodapp.view

import ProductViewModel
import RestaurantViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ProductAdapter
import com.ltb.orderfoodapp.viewmodel.CategoryViewModel
import com.ltb.orderfoodapp.viewmodel.ImageViewModel

class Home : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var categroyViewModel: CategoryViewModel
    private lateinit var restaurantView: RestaurantViewModel
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var darkTheme: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        enableEdgeToEdge()
        val nextSearch = findViewById<TextView>(R.id.txtSearch)
        val nextCart = findViewById<ImageButton>(R.id.nextCart)
        val nextMenu = findViewById<ImageButton>(R.id.nextMenu)
        setupNavigation(nextSearch, nextCart, nextMenu)
        productViewModel = ProductViewModel()
        categroyViewModel = CategoryViewModel()
        restaurantView = RestaurantViewModel()
        productViewModel.fetchData()
//        imageViewModel = ImageViewModel()
//        imageViewModel.fetchData()
//        imageViewModel.addToFirebase()
//        categroyViewModel.fetchData()
//        restaurantView.fetchData()
//        restaurantView.addToFirebase()
//        productViewModel.addToFirebase()
//        categroyViewModel.addToFirebase()

    }

    override fun onStart() {
        super.onStart()
        darkTheme = findViewById(R.id.darkTheme)
        setupGridView()
        setupTheme()
        darkTheme.setOnCheckedChangeListener { _, isChecked ->
            updateTheme(isChecked)
        }
    }

    private fun setupGridView() {
        val products = productViewModel.getProducts()
        val gridView = findViewById<GridView>(R.id.gridviewProduct)
        val adapter = ProductAdapter(this, products)
        gridView.adapter = adapter
    }

    private fun setupNavigation(nextSearch: TextView, nextCart: ImageButton, nextMenu: ImageButton) {
        // Chuyển sang trang tìm kiếm
        nextSearch.setOnClickListener {
            val nextSearch = Intent(this, Search::class.java)
            startActivity(nextSearch)
        }

        // Chuyển sang giỏ hàng
        nextCart.setOnClickListener {
            val nextCart = Intent(this, MyCart::class.java)
            startActivity(nextCart)
        }

        // Chuyển sang menu
        nextMenu.setOnClickListener {
            val nextMenu = Intent(this, MyMainMenu::class.java)
            startActivity(nextMenu)
        }
    }

    private fun setupTheme() {
        // Lấy chế độ hiện tại từ SharedPreferences
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)

        // Kiểm tra chế độ hiện tại của app, chỉ thay đổi nếu nó khác với chế độ đã lưu
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        val newMode = if (nightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        // Nếu chế độ hiện tại khác với chế độ đã lưu, mới thực hiện thay đổi
        if (currentMode != newMode) {
            AppCompatDelegate.setDefaultNightMode(newMode)
        }

        // Cập nhật trạng thái của Switch
        darkTheme.isChecked = nightMode
    }

    private fun updateTheme(isChecked: Boolean) {
        // Lưu trạng thái chế độ và cập nhật theme
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("night", isChecked)
        editor.apply()

        // Cập nhật lại chế độ theme khi người dùng thay đổi
        val newMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(newMode)
    }

}
