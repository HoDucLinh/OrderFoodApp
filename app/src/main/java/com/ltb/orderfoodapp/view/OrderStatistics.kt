package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.OrderAdapter
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO

class OrderStatistics : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_statistics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Lấy Spinner từ XML
        val spinner: Spinner = findViewById(R.id.spinner)
        val nameEditText: EditText = findViewById(R.id.Name)
        val dateEditText: EditText = findViewById(R.id.Date)
        val listView: ListView = findViewById(R.id.list_item)

        // Lấy dữ liệu từ database
        val categories = ProductDAO(this).getAllCategories()

        // Tạo ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Gắn adapter vào Spinner
        spinner.adapter = adapter
        findViewById<Button>(R.id.filterButton).setOnClickListener {
            val name = nameEditText.text.toString()
            val date = dateEditText.text.toString()
            val categoryId = if (spinner.selectedItemPosition >= 0) spinner.selectedItemPosition + 1 else null

            val orders = OrderDAO(this).getOrdersByFilters(name, date, categoryId)

//            val orderAdapter = OrderAdapter(this, orders)
//            listView.adapter = orderAdapter
        }


    }
}