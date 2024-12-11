package com.ltb.orderfoodapp.view

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ltb.orderfoodapp.R

class TrackingOrder : AppCompatActivity() {

    private var remainingTimeInSeconds: Int = 0
    private val handler = Handler()
    private lateinit var timeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_order)
        enableEdgeToEdge()

        // Lấy địa chỉ người dùng từ SharedPreferences
        val sharedPreferences = getSharedPreferences("locationPath", MODE_PRIVATE)
        val locationPath = sharedPreferences.getString("locationPath", "Unknown Location")

        // Hiển thị thông tin địa chỉ
        val title = findViewById<TextView>(R.id.title)
        title.text = "Orders are still being shipped to $locationPath"

        remainingTimeInSeconds = (10..30).random()

        timeTextView = findViewById(R.id.time)

        startCountdown()
    }

    private fun startCountdown() {
        val runnable = object : Runnable {
            override fun run() {
                if (remainingTimeInSeconds > 0) {
                    val minutes = remainingTimeInSeconds / 60
                    val seconds = remainingTimeInSeconds % 60
                    timeTextView.text = String.format("%02d:%02d", minutes, seconds)

                    remainingTimeInSeconds--
                    handler.postDelayed(this, 1000)
                } else {

                    val title = findViewById<TextView>(R.id.title)
                    title.text = "Your order has been successfully delivered!"
                }
            }
        }
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Dừng Handler khi Activity bị huỷ
        handler.removeCallbacksAndMessages(null)
    }
}
