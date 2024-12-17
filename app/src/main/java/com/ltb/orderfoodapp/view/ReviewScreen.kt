package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.RatingDAO
import com.ltb.orderfoodapp.data.model.Review
import com.ltb.orderfoodapp.data.model.User
import com.ltb.orderfoodapp.viewmodel.ReviewViewModel

class ReviewScreen : AppCompatActivity() {
    private lateinit var reviewList : MutableList<Review>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_review_screen)


        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val role = sharedPreferences.getString("role", "customer")
        val user = sharedPreferences.getString("user", "")

        val userObject = Gson().fromJson(user, User::class.java)
        val userId = userObject.getIdUser()

        val back = findViewById<ImageButton>(R.id.backButton)
        back.setOnClickListener {
            val destination = when {
                isLoggedIn && role == "customer" -> MyMainMenu::class.java
                isLoggedIn && role == "restaurant" -> SellerDashboardHome::class.java
                else -> SignIn::class.java
            }

            startActivity(Intent(this, destination))
        }

        val scrollLayout = findViewById<LinearLayout>(R.id.scrollLayout)
        val ratingDAO = ReviewViewModel(this)
        if(role == "customer"){
            reviewList =RatingDAO(this).getReviewsByUserId(userId)
        }else {
            reviewList = ratingDAO.reviewAll()
        }
            for (review in reviewList) {
            val reviewView = LayoutInflater.from(this).inflate(R.layout.review_unit, scrollLayout, false)

            val userNameTextView = reviewView.findViewById<TextView>(R.id.textView_username)
            val ratingBar = reviewView.findViewById<RatingBar>(R.id.ratingBar)
            val commentTextView = reviewView.findViewById<TextView>(R.id.textView_comment)

            userNameTextView.text = review.getFullName()
            ratingBar.rating = review.getRating()
            commentTextView.text = review.getComment()

            scrollLayout.addView(reviewView)
        }

    }
}