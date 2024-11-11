package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ViewPagerAdapter

class MyOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ltb.orderfoodapp.R.layout.activity_my_order)
//        setContentView(R.layout.fragment_ongoing)

//        ordersContainer = findViewById(R.id.ordersContainer)
        val backMainMenu = findViewById<ImageButton>(R.id.backMainMenu)
        val tabLayout = findViewById<TabLayout>(com.ltb.orderfoodapp.R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(com.ltb.orderfoodapp.R.id.viewPager)
//        val toolbar = findViewById<Toolbar>(com.ltb.orderfoodapp.R.id.toolbar)
        // Back main menu
        backMainMenu.setOnClickListener{
            val mainMenu = Intent(this,MyMainMenu::class.java)
            startActivity(mainMenu)
        }
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = ""

        // Thiết lập adapter cho ViewPager2
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Kết nối TabLayout với ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Ongoing" else "History"
        }.attach()

//        for (i in 0 until 10) { // Ví dụ thêm 5 sản phẩm
//            // Inflate layout cho từng sản phẩm
//            val view = LayoutInflater.from(this).inflate(R.layout.orders, ordersContainer, false)
//
//            // Thêm view vào productsContainer
//            ordersContainer.addView(view)
//        }
    }

}