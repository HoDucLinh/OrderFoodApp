package com.ltb.orderfoodapp.adapter

import android.annotation.SuppressLint
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
import com.ltb.orderfoodapp.data.dao.ImageDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.view.FoodDetail
import com.ltb.orderfoodapp.view.Home
import com.ltb.orderfoodapp.view.Search

class ProductAdapter(
    private val context: Context,
    private val products: MutableList<Product>,
) : BaseAdapter() {

    override fun getCount(): Int {
        return products.size
    }
    override fun getItem(position: Int): Any {
        return products[position]
    }

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

        val layout = if (context is Search) R.layout.product_search else R.layout.product
        val view = LayoutInflater.from(context).inflate(layout, parent, false)

        val imgProduct = view.findViewById<ImageView>(R.id.img_product)
        val productName = view.findViewById<TextView>(R.id.product_name)
        val productPrice = view.findViewById<TextView>(R.id.product_price)
        val productRating: View = if (context is Search) {
            view.findViewById<TextView>(R.id.ratingBar)
        } else {
            view.findViewById<RatingBar>(R.id.ratingBar)
        }

        val product = products[position]

        if (product.getImages().isNotEmpty() && product.getImages()[0] != null) {
            Glide.with(context)
                .load(product.getImages()[0])
                .error(R.drawable.cancel)
                .into(imgProduct)
        } else {
            Glide.with(context)
                .load(R.drawable.burger)
                .into(imgProduct)
        }

        productName.text = product.getName()
        productPrice.text = "${product.getPrice()}VND"

        if (productRating is RatingBar) {
            productRating.rating = product.getRating()
        } else if (productRating is TextView) {
            productRating.text = product.getRating().toString()
        }

        view.setOnClickListener {
            openFoodDetail(context, product)
        }
        return view
    }


    //     Mo trang food detail
    private fun openFoodDetail(context: Context, product: Product) {
        val intent = Intent(context, FoodDetail::class.java)
        val imageList: MutableList<String> = product.getImages()
        val imageListArrayList = ArrayList(imageList)
        intent.putExtra("id",product.getIdProduct())
        intent.putStringArrayListExtra("imageResource", imageListArrayList)
        intent.putExtra("name", product.getName())
//        intent.putExtra("storeName", product.getRestaurant())
        intent.putExtra("price", product.getPrice())
        intent.putExtra("rating", product.getRating())
        context.startActivity(intent)
    }
}
