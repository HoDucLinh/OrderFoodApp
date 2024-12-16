package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product
class StatusAdapter(
    private val context: Context,
    private val productDetails: List<GroupOrderAdapter.ProductWithStatus>
) : RecyclerView.Adapter<StatusAdapter.ProductViewHolder>() {

    // ViewHolder để giữ các view của mỗi item
    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProduct: ImageView = view.findViewById(R.id.image)
        val txtNameFood: TextView = view.findViewById(R.id.txtNameFood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_status, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productWithStatus = productDetails[position]
        val product = productWithStatus.product

        holder.txtNameFood.text = product.getName()

        if (product.getImages().isNotEmpty()) {
            Glide.with(context)
                .load(product.getImages()[0])
                .error(R.drawable.cancel)
                .into(holder.imageProduct)
        } else {
            Glide.with(context)
                .load(R.drawable.burger)
                .into(holder.imageProduct)
        }
    }

    override fun getItemCount(): Int {
        return productDetails.size
    }
}
