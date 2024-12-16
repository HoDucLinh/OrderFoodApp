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
    private lateinit var  authManager: AuthManager
    private lateinit var userDAO: UserDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        authManager = AuthManager(this)
        setContentView(R.layout.activity_sign_up)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)

        // Check email exist
        val userNameInput =  findViewById<TextInputLayout>(R.id.emailSignUp)
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
                        userNameInput.error = "Invalid email address"
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


        userDAO = UserDAO(this)
        signUpBtn.setOnClickListener{
            val fullName = findViewById<TextInputLayout>(R.id.userNameSignUp).editText?.text.toString()

            val email =userNameInput.editText?.text.toString()


            val phone = findViewById<TextInputLayout>(R.id.phoneSignUp).editText?.text.toString()
            val password = findViewById<TextInputLayout>(R.id.passwordSignUp).editText?.text.toString()
            val rePassword = findViewById<TextInputLayout>(R.id.rePasswordSignUp).editText?.text.toString()
            if(password == rePassword){
                val newUser = User(fullName = fullName, email = email, phoneNumber = phone, password = password)
                val cartDAO = CartDAO(this)
                println(newUser.getIdUser())
                var userID = userDAO.addUser(newUser)
                val cartID = cartDAO.insertCart(0, userID)
                userDAO.updateUserCartId(userID, cartID.toInt())
                authManager.createAccount(email,password)
                Toast.makeText(this, "Sign Up Success, Please Login", Toast.LENGTH_SHORT).show()
                val returnLogin = Intent( this, SignIn::class.java)
                startActivity(returnLogin)

            }

        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}