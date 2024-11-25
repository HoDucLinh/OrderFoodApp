package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.GridView
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.OrderAdapter
import com.ltb.orderfoodapp.adapter.ViewPagerAdapter
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.viewmodel.ProductCartViewModel

class MyOrder : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ltb.orderfoodapp.R.layout.activity_my_order)
//        setContentView(R.layout.fragment_ongoing)
        dbHelper = DatabaseHelper(this)
        val backMainMenu = findViewById<ImageButton>(R.id.backMainMenu)
        val tabLayout = findViewById<TabLayout>(com.ltb.orderfoodapp.R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(com.ltb.orderfoodapp.R.id.viewPager)
        // Back main menu
        backMainMenu.setOnClickListener{
            val mainMenu = Intent(this,MyMainMenu::class.java)
            startActivity(mainMenu)
        }
        // Thiết lập adapter cho ViewPager2
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Kết nối TabLayout với ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Ongoing" else "History"
        }.attach()

//        for (i in 0 until 10) { // Ví dụ thêm 5 sản phẩm
//            // Inflate layout cho từng sản phẩm
//            val view = LayoutInflater.from(this).inflate(R.layout.activity_my_order, ordersContainer, false)
//
//            // Thêm view vào productsContainer
//            ordersContainer.addView(view)
//        }
    }

}