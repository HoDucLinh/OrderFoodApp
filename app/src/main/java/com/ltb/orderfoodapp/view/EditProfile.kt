package com.ltb.orderfoodapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.User

class EditProfile : AppCompatActivity() {

    private lateinit var editFullName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var editBio: EditText
    private var userId: Int = -1 // ID của người dùng hiện tại

    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        userDAO = UserDAO(this)

        editFullName = findViewById(R.id.fullname)
        editEmail = findViewById(R.id.email)
        editPhone = findViewById(R.id.phone)
        editBio = findViewById(R.id.bio)

        // Lấy userID từ SharedPreferences
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userJson = sharedPreferences.getString("user", null)
        val userObject = Gson().fromJson(userJson, User::class.java)
        userId = userObject?.getIdUser() ?: -1

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show()
            return
        }

        val backmenu = findViewById<ImageButton>(R.id.backMenu)
        backmenu.setOnClickListener {
                val personalInformation = Intent(this, PersonalInformation::class.java)
                startActivity(personalInformation)
        }

        Log.d("EditProfile", "Current UserID: $userId")

        // Tải dữ liệu người dùng
        loadUserData()

        val btnSave: Button = findViewById(R.id.btn_saveInfor)
        btnSave.setOnClickListener {
            val profile = Intent(this,MyMainMenu ::class.java)
            saveUserProfile()
            startActivity(profile)
        }
    }

    private fun loadUserData() {
        val user = userDAO.getUserById(userId)
        if (user != null) {
            editFullName.setText(user.getFullName())
            editEmail.setText(user.getEmail())
            editPhone.setText(user.getPhoneNumber())
            editBio.setText(user.getBioInfor())
        } else {
            Toast.makeText(this, "Không thể tải thông tin người dùng!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserProfile() {
        val fullName = editFullName.text.toString()
        val email = editEmail.text.toString()
        val phone = editPhone.text.toString()
        val bio = editBio.text.toString()

        val result = userDAO.updateUser(userId, fullName, email, phone, bio)
        if (result) {
            Toast.makeText(this, "Thông tin đã được cập nhật!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Cập nhật không thành công!", Toast.LENGTH_SHORT).show()
        }
    }
}

