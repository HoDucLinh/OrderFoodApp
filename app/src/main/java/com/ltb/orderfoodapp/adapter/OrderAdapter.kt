package com.ltb.orderfoodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.view.HistoryFragment
import com.ltb.orderfoodapp.view.RateProductDialogFragment

class OrderAdapter(
    private val context: Context,
    private val products: List<Product>,
    private val fragment: Fragment
) : BaseAdapter() {

    private val orderDAO = OrderDAO(context)

    override fun getCount(): Int = products.size

    override fun getItem(position: Int): Any = products[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (products.isEmpty()) {
            Log.e("OrderAdapter", "Danh sách products rỗng.")
            return LayoutInflater.from(context).inflate(R.layout.item_no_product, parent, false)
        }

        if (position < 0 || position >= products.size) {
            Log.e("OrderAdapter", "Vị trí không hợp lệ: $position")
            return LayoutInflater.from(context).inflate(R.layout.item_no_product, parent, false)
        }

        val view = if (fragment is HistoryFragment) {
            LayoutInflater.from(context).inflate(R.layout.item_orders_history, parent, false).apply {
                setupHistoryView(position, this)
            }
        } else {
            LayoutInflater.from(context).inflate(R.layout.item_orders_ongoing, parent, false).apply {
                setupOngoingView(position, this)
            }
        }

        return view
    }

    private fun setupHistoryView(position: Int, view: View) {
        if (products.isEmpty() || position < 0 || position >= products.size) {
            Log.e("OrderAdapter", "Không có dữ liệu hợp lệ tại vị trí $position để thiết lập view lịch sử.")
            return
        }

        Log.d("OrderAdapter", "productsList size history: ${products.size}")
        val product = products[position]

        val ratingBtn = view.findViewById<Button>(R.id.rating)
        ratingBtn.setOnClickListener {
            val dialog = RateProductDialogFragment.newInstance(product.getIdProduct())
            fragment.parentFragmentManager.beginTransaction()
                .add(dialog, "RateProductDialog")
                .commit()
        }

        val productQuantity = orderDAO.getProductQuantityByProductId(product.getIdProduct())

        val orderImg = view.findViewById<ImageView>(R.id.order_img)

        Glide.with(context)
            .load(product.getImages().firstOrNull()?: R.drawable.burger)
            .error(R.drawable.cancel) // Default image
            .into(orderImg)

        val orderName = view.findViewById<TextView>(R.id.order_name)
        val orderPrice = view.findViewById<TextView>(R.id.order_price)
        val orderQuantity = view.findViewById<TextView>(R.id.order_quantity)

        orderName.text = product.getName()
        orderPrice.text = "${product.getPrice() * productQuantity}VND"
        orderQuantity.text = "$productQuantity Items"
    }

    private fun setupOngoingView(position: Int, view: View) {
        if (products.isEmpty() || position < 0 || position >= products.size) {
            Log.e("OrderAdapter", "Không có dữ liệu hợp lệ tại vị trí $position để thiết lập view đang hoạt động.")
            return
        }

        Log.d("OrderAdapter", "productsList size on going: ${products.size}")
        val product = products[position]
        val productQuantity = orderDAO.getProductQuantityByProductId(product.getIdProduct())

        val orderImg = view.findViewById<ImageView>(R.id.order_img)
        Glide.with(context)
            .load(product.getImages().firstOrNull() ?: R.drawable.burger)
            .error(R.drawable.cancel) // Default image
            .into(orderImg)

        val orderName = view.findViewById<TextView>(R.id.order_name)
        val orderPrice = view.findViewById<TextView>(R.id.order_price)
        val orderQuantity = view.findViewById<TextView>(R.id.order_quantity)

        orderName.text = product.getName()
        orderPrice.text = "${product.getPrice() * productQuantity}VND"
        orderQuantity.text = "$productQuantity Items"
    }
}
