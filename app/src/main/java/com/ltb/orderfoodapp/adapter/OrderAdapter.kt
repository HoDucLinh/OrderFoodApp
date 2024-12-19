package com.ltb.orderfoodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.view.HistoryFragment
import com.ltb.orderfoodapp.view.RateProductDialogFragment
import com.ltb.orderfoodapp.view.TrackingOrder

class OrderAdapter(
    private val context: Context,
    private val products: List<Pair<Product, Int>>, // Pair<Product, StatusId>
    private val fragment: Fragment,
    private val cartId: Int
) : BaseAdapter() {

    private val orderDAO = OrderDAO(context)

    override fun getCount(): Int = products.size

    override fun getItem(position: Int): Pair<Product, Int> = products[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (products.isEmpty()) {
            Log.e("OrderAdapter", "Danh sách products rỗng.")
            return LayoutInflater.from(context).inflate(R.layout.item_no_product, parent, false)
        }

        val (product, statusId) = products[position] // Lấy Product và statusId từ Pair

        val view = if (fragment is HistoryFragment) {
            LayoutInflater.from(context).inflate(R.layout.item_orders_history, parent, false).apply {
                setupHistoryView(this, product, statusId)
            }
        } else {
            LayoutInflater.from(context).inflate(R.layout.item_orders_ongoing, parent, false).apply {
                setupOngoingView(this, product, statusId)
            }
        }

        return view
    }

    private fun setupHistoryView(view: View, product: Product, statusId: Int) {
        Log.d("OrderAdapter", "Setting up history view for product: ${product.getName()} with status: $statusId")

        val reOrderButton = view.findViewById<Button>(R.id.btn_reorder)
        reOrderButton.setOnClickListener {
            orderDAO.addProductToCart(product.getIdProduct(), cartId)
            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
        }

        val ratingButton = view.findViewById<Button>(R.id.rating)
        ratingButton.setOnClickListener {
            val dialog = RateProductDialogFragment.newInstance(product.getIdProduct())
            fragment.parentFragmentManager.beginTransaction()
                .add(dialog, "RateProductDialog")
                .commit()
        }

        val productQuantity = orderDAO.getProductQuantityByProductId(product.getIdProduct())

        view.findViewById<TextView>(R.id.order_name).text = product.getName()
        view.findViewById<TextView>(R.id.order_price).text = "${product.getPrice() * productQuantity} VND"
        view.findViewById<TextView>(R.id.order_quantity).text = "$productQuantity Items"

        val orderImg = view.findViewById<ImageView>(R.id.order_img)
        Glide.with(context)
            .load(product.getImages().firstOrNull() ?: R.drawable.burger)
            .error(R.drawable.cancel) // Default image
            .into(orderImg)
    }

    private fun setupOngoingView(view: View, product: Product, statusId: Int) {
        Log.d("OrderAdapter", "Setting up ongoing view for product: ${product.getName()} with status: $statusId")

        val productQuantity = orderDAO.getProductQuantityByProductId(product.getIdProduct())

        view.findViewById<TextView>(R.id.order_name).text = product.getName()
        view.findViewById<TextView>(R.id.order_price).text = "${product.getPrice() * productQuantity} VND"
        view.findViewById<TextView>(R.id.order_quantity).text = "$productQuantity Items"


        val trackingOrderButton = view.findViewById<Button>(R.id.tracking)
        trackingOrderButton.setOnClickListener {
            val trackingIntent = Intent(context, TrackingOrder::class.java)
            trackingIntent.putExtra("statusId", statusId)
            context.startActivity(trackingIntent)
        }

        val orderImg = view.findViewById<ImageView>(R.id.order_img)
        Glide.with(context)
            .load(product.getImages().firstOrNull() ?: R.drawable.burger)
            .error(R.drawable.cancel)
            .into(orderImg)
    }
}
