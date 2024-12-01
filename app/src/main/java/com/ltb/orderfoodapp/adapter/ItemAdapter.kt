package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart

class ItemAdapter (private val context: Context,
                   val products: MutableList<Product>): BaseAdapter() {
    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_food_list, parent, false)

        val product = products[position]

        val imgProduct = view.findViewById<ImageButton>(R.id.imageButton2)
        val txtNameFood = view.findViewById<TextView>(R.id.txtNameFood)
        val txtNameRestaurant = view.findViewById<TextView>(R.id.txtNameRestaurant)
        val txtPrice = view.findViewById<TextView>(R.id.txtPrice)
        val ratingBar = view.findViewById<RatingBar>(R.id.txtRating)

        // Bind product data
        Glide.with(context).load(product.images.getOrNull(0) ?: R.drawable.burger).into(imgProduct)
        txtNameFood.text = product.name
        txtNameRestaurant.text = product.restaurant
        txtPrice.text = "${product.price} VND"
        ratingBar.rating = product.rating

        return view
    }


}