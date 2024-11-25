package com.ltb.orderfoodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import com.ltb.orderfoodapp.data.model.ProductCart

class OrderAdapter(
    private val context: Context,
    private val products: MutableList<ProductCart>,
) : BaseAdapter() {
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

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (products.isEmpty() || position < 0 || position >= products.size) {
            return TextView(parent?.context).apply {
                text = "No product"
            }
        }
        val view = LayoutInflater.from(context).inflate(R.layout.item_orders_ongoing, parent, false)
        // Kiem cac thanh phan trong layout cua product
        val orderImg = view.findViewById<ImageView>(R.id.order_img)
        val orderName = view.findViewById<TextView>(R.id.order_name)
        val orderPrice = view.findViewById<TextView>(R.id.order_price)
        val orderQuantity = view.findViewById<TextView>(R.id.order_quantity)


        // Lay vi tri hien tai
        val product = products[position]
        // Thiet lap giao dien
        Glide.with(context)
            .load(product.images[0])
            .into(orderImg);
        orderName.text = product.name
        orderPrice.text = "${product.price * product.quantity }VND"
        orderQuantity.text = "${product.quantity} Items"

//        view.setOnClickListener {
//            openFoodDetail(context, product)
//        }
        return view


    }
}
