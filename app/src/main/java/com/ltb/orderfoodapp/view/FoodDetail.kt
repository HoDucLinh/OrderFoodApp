package com.ltb.orderfoodapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product

class FoodDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_food_detail)
        getProductInfor()
        setProductInfor()
        val addToCart = findViewById<Button>(R.id.addCart)
        val backMain = findViewById<ImageView>(R.id.backMain)
        backMain.setOnClickListener{
            val home = Intent(this,Home::class.java)
            startActivity(home)
        }
        addToCart.setOnClickListener{
            Toast.makeText(this, "Them thanh cong", Toast.LENGTH_SHORT).show()

        }

    }
    fun getProductInfor()
    {
        val name = intent.getStringExtra("name") ?: ""
        val storeName = intent.getStringExtra("storeName") ?: ""
        val price = intent.getIntExtra("price",0)
        val imageResource = intent.getIntExtra("imageResource", 0)
        val rating = intent.getIntExtra("rating", 0)

        val product = Product(name, storeName,price,imageResource, rating)
    }
    fun setProductInfor(){


    }
}