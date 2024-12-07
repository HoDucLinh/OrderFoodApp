package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Category
class CategoryAdapter(
    private val context: Context,
    private val categories: MutableList<String>,
) : BaseAdapter() {
    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Any {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return 0 
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.categories,parent,false)
        val categoryName = view.findViewById<TextView>(R.id.categoryName)
        val category = categories[position]
        categoryName.text =  category
        return view
    }


}