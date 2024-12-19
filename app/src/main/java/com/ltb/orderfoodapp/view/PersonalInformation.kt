package com.ltb.orderfoodapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.User

class PersonalInformation : AppCompatActivity() {

    private lateinit var userDAO: UserDAO

    private lateinit var textViewFullName: TextView
    private lateinit var textViewFullName2: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewPhoneNumber: TextView
    private lateinit var textViewBio: TextView

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_information)

        userDAO = UserDAO(this)

        textViewFullName2 = findViewById(R.id.textView_fullName)
        textViewBio = findViewById(R.id.textView_bio)

        // Ánh xạ các TextView
        textViewFullName = findViewById(R.id.textView_fullNameValue)
        textViewEmail = findViewById(R.id.textView_emailValue)
        textViewPhoneNumber = findViewById(R.id.textView_phoneNumberValue)

        // Lấy userID từ SharedPreferences
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userJson = sharedPreferences.getString("user", null)
        val userObject = Gson().fromJson(userJson, User::class.java)
        userId = userObject?.getIdUser() ?: -1

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("PersonalInformation", "Current UserID: $userId")

        // Tải dữ liệu người dùng
        loadUserData()

        // Xử lý nút quay lại
        val backmenu = findViewById<ImageButton>(R.id.backMenu)
        backmenu.setOnClickListener {
            println("RoleID  " + userObject.getRoleId())
            if(userObject.getRoleId() == 3 ){
                val myMainMenu = Intent(this, Menu::class.java)
                startActivity(myMainMenu)
            }
            else {
            val myMainMenu = Intent(this, MyMainMenu::class.java)
            startActivity(myMainMenu)
        }}

        // Xử lý nút chỉnh sửa
        val editButton = findViewById<Button>(R.id.editInfor)
        editButton.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val user = userDAO.getUserById(userId)
        if (user != null) {
            textViewFullName.text = user.getFullName()
            textViewFullName2.text = user.getFullName()
            textViewEmail.text = user.getEmail()
            textViewPhoneNumber.text = user.getPhoneNumber()
            textViewBio.text = user.getBioInfor()
        } else {
            Toast.makeText(this, "Không thể tải thông tin người dùng!", Toast.LENGTH_SHORT).show()
        }
    }
}
