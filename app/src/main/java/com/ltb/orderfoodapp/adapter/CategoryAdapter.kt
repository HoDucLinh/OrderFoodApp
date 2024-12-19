package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ltb.orderfoodapp.R

class CategoryAdapter(
    private val context: Context,
    private val categories: MutableList<String>
) : BaseAdapter() {
    // Vị trí danh mục được chọn, mặc định là -1 (không chọn gì)
    var selectedPosition: Int = -1

    override fun getCount(): Int = categories.size

    override fun getItem(position: Int): String = categories[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.categories, parent, false)
        val categoryName = view.findViewById<TextView>(R.id.categoryName)
        categoryName.text = categories[position]

        // Thay đổi màu nền nếu đây là mục được chọn
        if (position == selectedPosition) {
            categoryName.setBackgroundColor(context.resources.getColor(R.color.selected_category_background, null))
        } else {
            categoryName.setBackgroundColor(context.resources.getColor(R.color.default_category_background, null))
        }


        return view
    }
}
