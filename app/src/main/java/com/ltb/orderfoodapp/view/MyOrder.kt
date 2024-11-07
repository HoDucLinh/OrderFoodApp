package com.ltb.orderfoodapp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ltb.orderfoodapp.adapter.ViewPagerAdapter

class MyOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ltb.orderfoodapp.R.layout.activity_my_order)
//        setContentView(R.layout.fragment_ongoing)

//        ordersContainer = findViewById(R.id.ordersContainer)

        val tabLayout = findViewById<TabLayout>(com.ltb.orderfoodapp.R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(com.ltb.orderfoodapp.R.id.viewPager)
        val toolbar = findViewById<Toolbar>(com.ltb.orderfoodapp.R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(com.ltb.orderfoodapp.R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.ltb.orderfoodapp.R.id.menu_more) {
            // Khi nhấn vào nút ba chấm, hiển thị PopupMenu
            val menuItemView = findViewById<View>(com.ltb.orderfoodapp.R.id.menu_more)
            showPopupMenu(menuItemView)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPopupMenu(anchor: View) {
        // Tạo một PopupMenu
        val popup = PopupMenu(this, anchor)

        // Nạp menu từ file XML
        popup.menuInflater.inflate(com.ltb.orderfoodapp.R.menu.popup_menu, popup.menu)

        // Đặt sự kiện cho các item trong PopupMenu
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                com.ltb.orderfoodapp.R.id.action_edit -> {
                    Toast.makeText(this, "Edit selected", Toast.LENGTH_SHORT).show()
                    true
                }
                com.ltb.orderfoodapp.R.id.action_delete -> {
                    Toast.makeText(this, "Delete selected", Toast.LENGTH_SHORT).show()
                    true
                }
                com.ltb.orderfoodapp.R.id.action_share -> {
                    Toast.makeText(this, "Share selected", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // Hiển thị PopupMenu
        popup.show()
    }
}