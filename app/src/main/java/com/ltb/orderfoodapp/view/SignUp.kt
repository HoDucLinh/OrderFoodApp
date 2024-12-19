package com.ltb.orderfoodapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.api.AuthManager
import com.ltb.orderfoodapp.data.dao.CartDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.User
import org.w3c.dom.Text


class SignUp : AppCompatActivity() {
    private lateinit var authManager: AuthManager
    private lateinit var userDAO: UserDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        authManager = AuthManager(this)
        setContentView(R.layout.activity_sign_up)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)

        // Check email exist
        val userNameInput = findViewById<TextInputLayout>(R.id.emailSignUp)
        val userDao = UserDAO(this)
        userNameInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                when {
                    email.isEmpty() -> {
                        userNameInput.error = null
                        println(userDao.isEmailExists(email))
                    }

                    !isValidEmail(email) -> {
                        userNameInput.error = "Email không hợp lệ"
                    }

                    userDao.isEmailExists(email) -> {
                        userNameInput.error = "Email đã được sử dụng"
                    }

                    else -> {
                        userNameInput.error = null
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val passwordInput = findViewById<TextInputLayout>(R.id.passwordSignUp)
        val rePasswordInput = findViewById<TextInputLayout>(R.id.rePasswordSignUp)

        rePasswordInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = passwordInput.editText?.text.toString().trim()
                val rePassword = s.toString().trim()

                // Kiểm tra nếu mật khẩu và xác nhận mật khẩu giống nhau
                if (password.isNotEmpty()) {
                    if (password != rePassword) {
                        rePasswordInput.error = "Mật khẩu không khớp"
                    } else {
                        rePasswordInput.error = null
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



        userDAO = UserDAO(this)
        signUpBtn.setOnClickListener {
            val fullName =
                findViewById<TextInputLayout>(R.id.userNameSignUp).editText?.text.toString()

            val email = userNameInput.editText?.text.toString()


            val phone = findViewById<TextInputLayout>(R.id.phoneSignUp).editText?.text.toString()
            val password = passwordInput.editText?.text.toString()
            val rePassword = rePasswordInput.editText?.text.toString()


            // Validate fields
            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != rePassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if email exists in the database
            if (userDAO.isEmailExists(email)) {
                Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password == rePassword) {
                val newUser = User(
                    fullName = fullName,
                    email = email,
                    phoneNumber = phone,
                    password = password
                )
                val cartDAO = CartDAO(this)
                println(newUser.getIdUser())
                var userID = userDAO.addUser(newUser)
                val cartID = cartDAO.insertCart(0, userID)
                userDAO.updateUserCartId(userID, cartID.toInt())
                authManager.createAccount(email, password)
                Toast.makeText(this, "Sign Up Success, Please Login", Toast.LENGTH_SHORT).show()
                val returnLogin = Intent(this, SignIn::class.java)
                startActivity(returnLogin)

            }

        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}