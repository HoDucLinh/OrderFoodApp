package com.ltb.orderfoodapp.view

import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.model.Image
import com.ltb.orderfoodapp.data.model.Status

class TrackingOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_order)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("locationPath", MODE_PRIVATE)
        val locationPath = sharedPreferences.getString("locationPath", "Unknown Location")

        val statusId  = intent.getIntExtra("statusId",1)
        loadGifBasedOnStatus(statusId)
        val title = findViewById<TextView>(R.id.title)
        title.text = getOrderStatusMessage(statusId,locationPath.toString())
        val back = findViewById<ImageButton>(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

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
            Status.PROCESSING.id -> "https://cdn-icons-gif.flaticon.com/9820/9820038.gif"
            Status.SHIPPED.id -> "https://cdn-icons-gif.flaticon.com/15578/15578437.gif"
            else -> "https://example.com/default.gif"
        }
    }

    private fun getOrderStatusMessage(statusId: Int, locationPath: String): String {
        return when (statusId) {
            1 -> "Your order is currently being processed.\nIt will soon be shipped to $locationPath."
            2 -> "Your order is on the way!\nIt is being shipped to $locationPath."
            else -> "Unknown status. Please check again."
        }
    }

}
