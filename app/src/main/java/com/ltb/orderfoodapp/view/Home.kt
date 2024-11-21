package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ProductAdapter
import com.ltb.orderfoodapp.viewmodel.ProductViewModel
import java.time.LocalTime

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
        var user =  intent.getStringExtra("userId")


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
        nextMenu.setOnClickListener {
            val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            if (isLoggedIn) {

                val profileIntent = Intent(this, MyMainMenu::class.java)
                startActivity(profileIntent)
            } else {
                // Nếu chưa đăng nhập, chuyển về Login
                val loginIntent = Intent(this, SignIn::class.java)
                startActivity(loginIntent)
            }
        }



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

    override fun onDestroy() {
        super.onDestroy()
        productViewModel.close()
    }

    private fun setupGridViewProduct() {
        val products = productViewModel.getProducts()
        val gridView = findViewById<GridView>(R.id.gridviewProduct)
        val adapter = ProductAdapter(this, products)
        gridView.adapter = adapter
    }

}