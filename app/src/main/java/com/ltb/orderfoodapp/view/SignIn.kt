package com.ltb.orderfoodapp.view


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.ltb.orderfoodapp.R

class SignIn : Activity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val forgotPassword = findViewById<TextView>(R.id.forgotPass)
        val signUpBtn = findViewById<TextView>(R.id.signUp)
        //Lay userName, password de login
        loginBtn.setOnClickListener{
            val userNameLogin = findViewById<EditText>(R.id.userNameLogin).getText().toString()
            val passwordLogin = findViewById<EditText>(R.id.passwordLogin).getText().toString()
            if(userNameLogin!=""&&passwordLogin!=""){
                signIn(userNameLogin,passwordLogin)
            }else Toast.makeText(this, "Please enter email, password", Toast.LENGTH_SHORT).show()

        }
        // Chuyen sang trang quen mat khau
        forgotPassword.setOnClickListener{
           val forgot = Intent(this, FortgotPassword::class.java)
            startActivity(forgot)
        }
        // Chuyen sang signUp
        signUpBtn.setOnClickListener{
            val signUp = Intent(this, SignUp::class.java)
            startActivity(signUp)
        }


    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }

    }
    // [END on_start_check_user]

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val homePage = Intent(this, Home::class.java)
                    startActivity(homePage)
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        baseContext,
                        "Login Error.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun reload() {
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}