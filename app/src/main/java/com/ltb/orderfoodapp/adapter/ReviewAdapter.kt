package com.ltb.orderfoodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Review

class ReviewAdapter(
    private val context: Context,
    private val reviews: List<Review>
) : BaseAdapter() {
    override fun getCount(): Int = reviews.size

    override fun getItem(position: Int): Any = reviews[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.review_unit, parent, false)

        // Kết nối các thành phần trong layout của review
        val userName = view.findViewById<TextView>(R.id.textView_username)
        val userComment = view.findViewById<TextView>(R.id.textView_comment)
        val userRating = view.findViewById<RatingBar>(R.id.ratingBar)

        // Lấy review hiện tại
        val review = getItem(position) as Review

        // Đổ dữ liệu vào các thành phần
        userName.text = review.getFullName()
        userComment.text = review.getComment()
        userRating.rating = review.getRating().toFloat()

        return view
    }
}
