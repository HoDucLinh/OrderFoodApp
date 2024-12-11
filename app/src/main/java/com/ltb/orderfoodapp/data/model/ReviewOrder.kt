package com.ltb.orderfoodapp.data.model

import java.util.Date

class ReviewOrder(
    private var idReviewOrder: Int,
    private var rating: Float,
    private var comment: String,
    private var reviewDate: Date,
    private var orderId: Int
) {
    // Constructor không tham số
    constructor() : this(0, 0f, "", Date(), 0)

    // Getter và Setter cho idReviewOrder
    fun getIdReviewOrder(): Int = idReviewOrder
    fun setIdReviewOrder(idReviewOrder: Int) {
        this.idReviewOrder = idReviewOrder
    }

    // Getter và Setter cho rating
    fun getRating(): Float = rating
    fun setRating(rating: Float) {
        this.rating = rating
    }

    // Getter và Setter cho comment
    fun getComment(): String = comment
    fun setComment(comment: String) {
        this.comment = comment
    }

    // Getter và Setter cho reviewDate
    fun getReviewDate(): Date = reviewDate
    fun setReviewDate(reviewDate: Date) {
        this.reviewDate = reviewDate
    }

    // Getter và Setter cho orderId
    fun getOrderId(): Int = orderId
    fun setOrderId(orderId: Int) {
        this.orderId = orderId
    }
}
