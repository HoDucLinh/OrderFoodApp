package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.view.FoodDetail

class ProductAdapter(private val context: Context, private val products: MutableList<Product>) : BaseAdapter() {

    // Trả về số lượng sản phẩm
    override fun getCount(): Int {
        return products.size
    }

    // Trả về sản phẩm ở vị trí 'position'
    override fun getItem(position: Int): Any {
        return products[position]
    }

    // Trả về ID của sản phẩm (ở đây sử dụng vị trí làm ID)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.product, parent, false)
        // Kiem cac thanh phan trong layout cua product
        val imgProduct = view.findViewById<ImageView>(R.id.img_product)
        val productName = view.findViewById<TextView>(R.id.product_name)
        val storeName = view.findViewById<TextView>(R.id.store_name)
        val productPrice = view.findViewById<TextView>(R.id.product_price)
        val productRating = view.findViewById<RatingBar>(R.id.ratingBar)

        // Lay vi tri hien tai
        val product = products[position]
        // Thiet lap giao dien

        Glide.with(context)
            .load(product.imageResource)
            .into(imgProduct);
        productName.text = product.name
        storeName.text = product.storeName
        productPrice.text = product.price.toString()
        productRating.rating = product.rating

        view.setOnClickListener {
            openFoodDetail(context, product)
        }

        return view
    }
    // Mo trang food detail
    private fun openFoodDetail(context: Context, product: Product) {
        val intent = Intent(context, FoodDetail::class.java)
        intent.putExtra("imageResource", product.imageResource)
        intent.putExtra("name", product.name)
        intent.putExtra("storeName", product.storeName)
        intent.putExtra("price", product.price)
        intent.putExtra("rating", product.rating)
        context.startActivity(intent)
    }
}
