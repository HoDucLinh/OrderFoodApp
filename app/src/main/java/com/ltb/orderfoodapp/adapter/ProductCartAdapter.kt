package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.ProductCart
import com.ltb.orderfoodapp.view.FoodDetail
import com.ltb.orderfoodapp.view.MyCart

class ProductCartAdapter(
    private val context: Context,
    val productCartList: MutableList<ProductCart>
) : RecyclerView.Adapter<ProductCartAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imageProduct)
        val productName: TextView = itemView.findViewById(R.id.nameProduct)
        val productPrice: TextView = itemView.findViewById(R.id.priceProduct)
        val quantity: TextView = itemView.findViewById(R.id.numberProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productCart = productCartList[position]

        // Hiển thị thông tin sản phẩm
        holder.productName.text = productCart.name
        holder.productPrice.text = "${productCart.price} VND"
        holder.quantity.text = "${productCart.quantity}"

        // Kiểm tra và tải ảnh
        if (productCart.images.isNotEmpty()) {
            Glide.with(context)
                .load(productCart.images[0])
                .into(holder.imgProduct)
        }

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FoodDetail::class.java)
            intent.putExtra("idProduct", productCart.productId)
            intent.putExtra("name", productCart.name)
            intent.putExtra("price", productCart.price)
            intent.putExtra("rating", productCart.rating)
            intent.putStringArrayListExtra("imageResource", ArrayList(productCart.images))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productCartList.size
    }
}
