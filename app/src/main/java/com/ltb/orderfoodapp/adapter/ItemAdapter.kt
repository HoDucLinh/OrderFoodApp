package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.view.EditItem

class ItemAdapter(
    private val context: Context,
    val products: MutableList<Product>
) : BaseAdapter() {
    override fun getCount(): Int {
        return products.size
    }

    override fun getItem(position: Int): Any {
        return products[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_food_list, parent, false
        )

        val product = products[position]

        // Gán dữ liệu
        val txtNameFood = view.findViewById<TextView>(R.id.txtNameFood)
        val txtCate = view.findViewById<TextView>(R.id.txtCate)
        val txtPrice = view.findViewById<TextView>(R.id.txtPrice)
        val image = view.findViewById<ImageView>(R.id.image)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)

        txtNameFood.text = product.getName()
        txtCate.text = product.getCategory()
        txtPrice.text = "${product.getPrice()}VND"

        // Sử dụng Glide để hiển thị hình ảnh
        Glide.with(context)
            .load(product.getImages().firstOrNull())
            .into(image)

        // Xử lý nút Edit
        btnEdit.setOnClickListener {
            openEditItemActivity(context, product.getIdProduct())
        }

        return view
    }

    private fun openEditItemActivity(context: Context, productId: Int) {
        val intent = Intent(context, EditItem::class.java)
        intent.putExtra("product_id", productId)
        context.startActivity(intent)
    }
}
