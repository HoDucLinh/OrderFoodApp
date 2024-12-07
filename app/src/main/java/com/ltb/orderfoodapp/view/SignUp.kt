package com.ltb.orderfoodapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
        userDAO = UserDAO(this)
        signUpBtn.setOnClickListener{
            val fullName = findViewById<TextInputLayout>(R.id.userNameSignUp).editText?.text.toString()
            val email = findViewById<TextInputLayout>(R.id.emailSignUp).editText?.text.toString()
            val phone = findViewById<TextInputLayout>(R.id.phoneSignUp).editText?.text.toString()
            val password = findViewById<TextInputLayout>(R.id.passwordSignUp).editText?.text.toString()
            val rePassword = findViewById<TextInputLayout>(R.id.rePasswordSignUp).editText?.text.toString()
            if(password == rePassword){
                val newUser = User(fullName = fullName, email = email, phoneNumber = phone, password = password)
                val cartDAO = CartDAO(this)
                println(newUser.idUser)
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
}