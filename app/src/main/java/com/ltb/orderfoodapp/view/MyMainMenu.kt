package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.api.AuthManager
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.User

class MyMainMenu : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var auth: AuthManager

    private lateinit var userDAO: UserDAO

    private lateinit var textViewFullName: TextView
    private lateinit var textViewBio: TextView

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        userDAO = UserDAO(this)


        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_main_menu)

        textViewFullName = findViewById(R.id.textView_fullName)
        textViewBio = findViewById(R.id.textView_bio)

        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userJson = sharedPreferences.getString("user", null)
        val userObject = Gson().fromJson(userJson, User::class.java)
        userId = userObject?.idUser ?: -1

        if (userId == -1) {
            // Xử lý trường hợp không tìm thấy user
            textViewFullName.text = "Guest"
            textViewBio.text = "Welcome to the app"
        } else {
            // Đổ dữ liệu người dùng
            loadUserData()
        }

        val btnPersonalInfor = findViewById<TextView>(R.id.personalInfo)
        btnPersonalInfor.setOnClickListener{
            val personalInfor = Intent(this, PersonalInformation::class.java)
            startActivity(personalInfor)
        }
        val backHome = findViewById<ImageButton>(R.id.backHome)
        backHome.setOnClickListener{
            val home = Intent(this, Home::class.java)
            startActivity(home)
        }
        // Chuyen sang my orders
        val myOrders = findViewById<TextView>(R.id.myOrders)
        myOrders.setOnClickListener{
            val myOrders = Intent(this, MyOrder::class.java)
            startActivity(myOrders)
        }
        // Chuyen sang my address
        val address = findViewById<TextView>(R.id.address)
        address.setOnClickListener{
            val editAddress = Intent(this, MyAddress::class.java)
            startActivity(editAddress)
        }
        // Chuyen sang notification
        val notification = findViewById<TextView>(R.id.notification)
        notification.setOnClickListener{
            val notification = Intent(this, Notification::class.java)
            startActivity(notification)
        }
        val paymentmethod = findViewById<TextView>(R.id.paymentmethod)
        paymentmethod.setOnClickListener{
            val paymentmethod = Intent(this, PaymentMethod::class.java)
            startActivity(paymentmethod)
        }
        // Logout ra khoi nguoi dung hien tai
        val logout = findViewById<TextView>(R.id.logout)
        logout.setOnClickListener{
            auth = AuthManager(this)
            auth.logout()
        }


    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val user = userDAO.getUserById(userId)
        if (user != null) {
            textViewFullName.text = user.fullName
            textViewBio.text = user.bioInfor
        } else {
            textViewFullName.text = "Unknown User"
            textViewBio.text = "No bio available"
        }
    }
}