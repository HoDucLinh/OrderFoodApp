package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart
import com.ltb.orderfoodapp.view.HistoryFragment
import com.ltb.orderfoodapp.view.OngoingFragment

class OrderAdapter(
    private val context: Context,
    private val products: MutableList<ProductCart>,
    private val fragment: Fragment
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
        var view = LayoutInflater.from(context).inflate(R.layout.item_orders_ongoing, parent, false)
        if(fragment is HistoryFragment){
            view = LayoutInflater.from(context).inflate(R.layout.item_orders_history, parent, false)
        }
        // Kiem cac thanh phan trong layout cua product
        val orderImg = view.findViewById<ImageView>(R.id.order_img)
        val orderName = view.findViewById<TextView>(R.id.order_name)
        val orderPrice = view.findViewById<TextView>(R.id.order_price)
        val orderQuantity = view.findViewById<TextView>(R.id.order_quantity)
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

        // Lay vi tri hien tai
        val product = products[position]
        // Thiet lap giao dien
        Glide.with(context)
            .load(product.images[0])
            .into(orderImg);
        orderName.text = product.name
        orderPrice.text = "${product.price * product.quantity }VND"
        orderQuantity.text = "${product.quantity} Items"
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

//        view.setOnClickListener {
//            openFoodDetail(context, product)
//        }
        return view


    }
}
