package com.ltb.orderfoodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.OrderDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.ProductCart
import com.ltb.orderfoodapp.view.HistoryFragment
import com.ltb.orderfoodapp.view.OngoingFragment
import com.ltb.orderfoodapp.view.RateProductDialogFragment

class OrderAdapter(
    private val context: Context,
    private val products: MutableList<ProductCart>,
    private val fragment: Fragment,
    private lateinit var orderDAO: OrderDAO
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
            return TextView(parent.context).apply {
                text = "No product"
            }
        }
        var view = LayoutInflater.from(context).inflate(R.layout.item_orders_ongoing, parent, false)
        val orderImg = view.findViewById<ImageView>(R.id.order_img)
        val orderName = view.findViewById<TextView>(R.id.order_name)
        val orderPrice = view.findViewById<TextView>(R.id.order_price)
        val orderQuantity = view.findViewById<TextView>(R.id.order_quantity)
//        val orderDate = view.findViewById<TextView>(R.id.order_date)
        if(fragment is HistoryFragment){
            view = LayoutInflater.from(context).inflate(R.layout.item_orders_history, parent, false)

            val ratingBtn = view.findViewById<Button>(R.id.rating)

            ratingBtn.setOnClickListener {
                if (fragment is HistoryFragment) {
                    val productId = products[position].getProductId() // Lấy ID của sản phẩm tại vị trí hiện tại
                    val dialog = RateProductDialogFragment.newInstance(productId)
                    // Thay vì gọi supportFragmentManager, dùng fragment's childFragmentManager hoặc parentFragmentManager
                    fragment.parentFragmentManager.beginTransaction().add(dialog, "RateProductDialog").commit()
                }
            }

            val product = products[position]
            val product2 =
            // Thiet lap giao dien
            if (product.images.isNotEmpty() && product.images[0] != null) {
                Glide.with(context)
                    .load(product.images[0])
                    .error(R.drawable.cancel) // ảnh mặc định
                    .into(orderImg);
            } else {
                Glide.with(context)
                    .load(R.drawable.burger) // ảnh mặc định
                    .into(orderImg);
            }
            orderName.text = product.getName()
            orderPrice.text = "${product.getPrice() * product.getQuantity() }VND"
            orderQuantity.text = "${product.getQuantity()} Items"
//        orderDate.text = "Date"

//        view.setOnClickListener {
//            openFoodDetail(context, product)
//        }
            return view
        }


        // Lay vi tri hien tai
        val product = products[position]
        // Thiet lap giao dien
        if (product.images.isNotEmpty() && product.images[0] != null) {
            Glide.with(context)
                .load(product.images[0])
                .error(R.drawable.cancel) // ảnh mặc định
                .into(orderImg);
        } else {
            Glide.with(context)
                .load(R.drawable.burger) // ảnh mặc định
                .into(orderImg);
        }
        orderName.text = product.getName()
        orderPrice.text = "${product.getPrice() * product.getQuantity() }VND"
        orderQuantity.text = "${product.getQuantity()} Items"
//        orderDate.text = "Date"

//        view.setOnClickListener {
//            openFoodDetail(context, product)
//        }
        return view


    }
}