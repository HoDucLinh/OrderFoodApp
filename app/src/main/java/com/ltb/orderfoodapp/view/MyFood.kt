package com.ltb.orderfoodapp.view

//import ProductViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ItemAdapter
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.viewmodel.ProductViewModel

class MyFood : AppCompatActivity() {
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var productList: List<Product>
    private lateinit var listView: ListView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_food)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAdd = findViewById<Button>(R.id.btnAddNew)
        btnAdd.setOnClickListener {
            val newItem = Intent(this, AddNewItems::class.java)
            startActivity(newItem)
        }

        listView = findViewById(R.id.listitem)

        // Sử dụng ViewModelProvider để khởi tạo ViewModel
        val viewModel = ProductViewModel(this)

        // Fetch products of restaurant 1
        productList = viewModel.getProductsByRestaurant("")

        // Initialize and set adapter
        itemAdapter = ItemAdapter(this, productList.toMutableList())
        listView.adapter = itemAdapter

        // Optional: Update total items TextView
        val totalItemsTextView: TextView = findViewById(R.id.textView6)
        totalItemsTextView.text = "Total ${productList.size} items"

        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener{
            val myBack = Intent(this, Menu::class.java)
            startActivity(myBack)
        }
    }
}
