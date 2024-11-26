package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.ProductCartDAO
import com.ltb.orderfoodapp.data.model.ProductCart
import com.ltb.orderfoodapp.view.FoodDetail
import com.ltb.orderfoodapp.view.MyCart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductCartAdapter(
    private val context: Context,
    val productCartList: MutableList<ProductCart>
) : RecyclerView.Adapter<ProductCartAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.imageProduct)
        val productName: TextView = itemView.findViewById(R.id.nameProduct)
        val productPrice: TextView = itemView.findViewById(R.id.priceProduct)
        val quantity: TextView = itemView.findViewById(R.id.numberProduct)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productCart = productCartList[position]

        // Hiển thị thông tin sản phẩm
        holder.productName.text = productCart.name
        val totalPrice = productCart.price * productCart.quantity
        holder.productPrice.text = "${totalPrice} VND"
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
        // Xử lý sự kiện click nút Delete
        holder.btnDelete.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val cartDAO = ProductCartDAO(context)
                cartDAO.deleteProduct(productCart.productId) // Xóa sản phẩm khỏi database

                withContext(Dispatchers.Main) {
                    productCartList.removeAt(position) // Xóa sản phẩm khỏi danh sách
                    notifyItemRemoved(position) // Cập nhật RecyclerView
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return productCartList.size
    }
}
