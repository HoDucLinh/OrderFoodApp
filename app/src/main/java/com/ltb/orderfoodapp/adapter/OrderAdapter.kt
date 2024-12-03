package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Order
import java.text.SimpleDateFormat

class OrderAdapter(private val context: Context, private val orders: List<Order>) :
    ArrayAdapter<Order>(context, R.layout.item_statistics, orders) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_statistics, parent, false)
        val order = orders[position]

        // Gắn các view trong activity_item_statistics
        val categoryTextView: TextView = view.findViewById(R.id.cate)
        val statusTextView: TextView = view.findViewById(R.id.orderStatus)
        val nameTextView: TextView = view.findViewById(R.id.nameProduct)
        val priceTextView: TextView = view.findViewById(R.id.priceProduct)
        val dateTextView: TextView = view.findViewById(R.id.dateOrder)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)

        // Gán giá trị từ đơn hàng
        val firstOrderDetail = order.orderDetails.firstOrNull()

        if (firstOrderDetail != null) {
            categoryTextView.text = "Category: ${firstOrderDetail.productId}" // You can replace this with actual category
            nameTextView.text = "Product: ${firstOrderDetail.productId}" // Replace with actual product name
            ratingBar.rating = firstOrderDetail.totalPrice // Replace with rating (if available)
        } else {
            categoryTextView.text = "Category: N/A"
            nameTextView.text = "Product: N/A"
            ratingBar.rating = 0f
        }

        statusTextView.text = order.orderStatus
        priceTextView.text = "Total: $${order.totalAmount}"
        dateTextView.text = "Date: ${SimpleDateFormat("dd MMM, yyyy").format(order.orderDate)}"

        return view
    }
}

