package com.ltb.orderfoodapp.data.model

data class Review(
    private var fullName: String,
    private var rating: Int,
    private var comment: String,
    private var reviewDate: String
) {
    // Getter và Setter cho fullName
    fun getFullName(): String {
        return fullName
    }

    fun setFullName(value: String) {
        fullName = value
    }

    // Getter và Setter cho rating
    fun getRating(): Int {
        return rating
    }

    fun setRating(value: Int) {
        rating = value
    }

    // Getter và Setter cho comment
    fun getComment(): String {
        return comment
    }

    fun setComment(value: String) {
        comment = value
    }

    // Getter và Setter cho reviewDate
    fun getReviewDate(): String {
        return reviewDate
    }

    fun setReviewDate(value: String) {
        reviewDate = value
    }
}
