package com.ltb.orderfoodapp.view

import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Status

class TrackingOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_order)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("locationPath", MODE_PRIVATE)
        val locationPath = sharedPreferences.getString("locationPath", "Unknown Location")



        loadGifBasedOnStatus(1)
        val title = findViewById<TextView>(R.id.title)
        title.text = "Orders are still being shipped to $locationPath.\n Please waiting a minutes! "
    }
    private fun loadGifBasedOnStatus(statusId: Int) {
        val gifUrl = getGifUrlForStatus(statusId)

        val imageView = findViewById<ImageView>(R.id.imageViewGif)
        Glide.with(this)
            .asGif()
            .load(gifUrl)
            .into(imageView)
    }

    private fun getGifUrlForStatus(statusId: Int): String {
        return when (statusId) {
            Status.PROCESSING.id -> "https://example.com/processing.gif"
            Status.PACKAGING.id -> "https://example.com/packaging.gif"
            Status.SHIPPED.id -> "https://example.com/shipped.gif"
            Status.DELIVERED.id -> "https://example.com/delivered.gif"
            else -> "https://example.com/default.gif"
        }
    }
}
