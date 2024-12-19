package com.ltb.orderfoodapp.viewmodel

import android.content.Context
import com.ltb.orderfoodapp.data.dao.RatingDAO
import com.ltb.orderfoodapp.data.model.Review

class ReviewViewModel(context: Context) {

    private val ratingDAO = RatingDAO(context)
    private var reviews: MutableList<Review> = mutableListOf()


    init {
        fetchReview()
    }

    private fun fetchReview() {
        reviews = ratingDAO.getAllReviews()
    }
    fun reviewCount() : Int{
        return reviews.size
    }
    fun reviewAll() : MutableList<Review>{
        return reviews
    }

    fun close() {
        ratingDAO.close()
    }

}
