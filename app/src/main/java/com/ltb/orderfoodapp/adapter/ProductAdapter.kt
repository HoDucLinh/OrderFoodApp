package com.ltb.orderfoodapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.view.FoodDetail

class ProductAdapter(
    private val context: Context,
    private val products: MutableList<Product>
) : BaseAdapter() {

    private val db = FirebaseFirestore.getInstance()

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

    // Chuyển đổi view của từng item trong GridView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.product, parent, false)

        val imgProduct = view.findViewById<ImageView>(R.id.img_product)
        val productName = view.findViewById<TextView>(R.id.product_name)
        val storeName = view.findViewById<TextView>(R.id.store_name)
        val productPrice = view.findViewById<TextView>(R.id.product_price)
        val productRating = view.findViewById<RatingBar>(R.id.ratingBar)
        val product = products[position]
        loadImage(product.image_id, imgProduct)
        productName.text = product.name
        loadRestaurantName(product.restaurant_id, storeName)
        productPrice.text = "${product.price} VND"
        productRating.rating = product.rating

        view.setOnClickListener {
            openFoodDetail(context, product)
        }

        return view
    }

    // Hàm tải hình ảnh từ Firestore vào ImageView sử dụng Glide
// Hàm tải hình ảnh từ Firestore vào ImageView sử dụng Glide
    private fun loadImage(image_id: DocumentReference, imgView: ImageView) {
        image_id.get()
            .addOnSuccessListener { snapshot ->
                val imageUrls = snapshot.get("value") as? List<String>
                Log.d("ImageUrls", imageUrls.toString())
                if (!imageUrls.isNullOrEmpty()) {

                    val imageUrl = imageUrls[0]
                    Glide.with(context)
                        .load(imageUrl)
                        .into(imgView)
                } else {

                }
            }
            .addOnFailureListener {

            }
    }

    // Hàm lấy tên nhà hàng từ Firestore
    private fun loadRestaurantName(restaurantRef: DocumentReference, storeNameView: TextView) {
        restaurantRef.get()
            .addOnSuccessListener { snapshot ->
                val restaurantName = snapshot.getString("name") ?: "Unknown Restaurant"
                storeNameView.text = restaurantName
            }
            .addOnFailureListener {
                storeNameView.text = "Unknown Restaurant"
            }
    }

    // Mở màn hình chi tiết món ăn
    private fun openFoodDetail(context: Context, product: Product) {
        val intent = Intent(context, FoodDetail::class.java)
        // Truyền tất cả các thông tin cần thiết vào màn hình chi tiết
        intent.putExtra("imageResource", product.image_id.path) // Truyền đường dẫn của DocumentReference
        intent.putExtra("name", product.name)
        intent.putExtra("storeName", product.restaurant_id.id) // Truyền ID của restaurant
        intent.putExtra("price", product.price)
        intent.putExtra("rating", product.rating)
        intent.putExtra("description", product.description)
        intent.putExtra("categoryId", product.category_id.path) // Truyền đường dẫn của category
        intent.putExtra("restaurantId", product.restaurant_id.path) // Truyền đường dẫn của restaurant
        context.startActivity(intent)
    }

    // Cập nhật danh sách sản phẩm mới
    fun updateProductList(newList: List<Product>) {
        products.clear()
        products.addAll(newList)
        notifyDataSetChanged()
    }
}
