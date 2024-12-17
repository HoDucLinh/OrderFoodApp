@file:Suppress("DEPRECATION")

package com.ltb.orderfoodapp.view

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.api.AuthManager
import com.ltb.orderfoodapp.data.api.AuthManager.Companion.RC_SIGN_IN

class SignIn : AppCompatActivity() {
    private lateinit var auth: AuthManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = AuthManager(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val forgotPassword = findViewById<TextView>(R.id.forgotPass)
        val signUpBtn = findViewById<TextView>(R.id.signUp)
        val sigInFacebook = findViewById<ImageView>(R.id.facebook)
        val signInGoogle = findViewById<ImageView>(R.id.google)
        val signInGithub = findViewById<ImageView>(R.id.github)

        val userNameInput = findViewById<TextInputLayout>(R.id.userNameLogin)
        val passwordInput = findViewById<TextInputLayout>(R.id.passwordLogin)

        userNameInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (email.isNotEmpty() && !isValidEmail(email)) {
                    userNameInput.error = "Invalid email address"
                } else {
                    userNameInput.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        passwordInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isEmpty()) {
                    passwordInput.error = "Password cannot be empty"
                } else {
                    passwordInput.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Xử lý sự kiện click của loginBtn
        loginBtn.setOnClickListener {
            val userNameLogin = userNameInput.editText?.text.toString()
            val passwordLogin = passwordInput.editText?.text.toString()

            if (userNameLogin.isNotEmpty() && passwordLogin.isNotEmpty()) {
                if (isValidEmail(userNameLogin)) {
                    showLoadingDialog()
                    auth.authEmail(userNameLogin, passwordLogin) {
                        status, message ->
                            if(status){
                                dismissLoadingDialog()
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            }
                        else {
                            dismissLoadingDialog()
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    dismissLoadingDialog()
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Chuyển sang trang quên mật khẩu
        forgotPassword.setOnClickListener {
            val forgot = Intent(this, FortgotPassword::class.java)
            startActivity(forgot)
        }

        // Chuyển sang trang đăng ký
        signUpBtn.setOnClickListener {
            val signUp = Intent(this, SignUp::class.java)
            startActivity(signUp)
        }

        // Đăng nhập với Facebook
        sigInFacebook.setOnClickListener {
            Toast.makeText(this, "Tinh nang dang phat trien thu lai sau", Toast.LENGTH_SHORT).show()
//                AuthFacebook.login(this) { user ->
//                    if (user != null) {
//
//                    } else {
//
//                    }
//                }
        }
        signInGithub.setOnClickListener {
            Toast.makeText(this, "Tinh nang dang phat trien thu lai sau", Toast.LENGTH_SHORT).show()
        }
        signInGoogle.setOnClickListener {
            signIn()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                showLoadingDialog()
                auth.firebaseAuthWithGoogle(account.idToken!!){
                    status, message ->
                    if(status){
                        dismissLoadingDialog()
                    }
                    else {
                        dismissLoadingDialog()
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun showLoadingDialog() {
        val builder = AlertDialog.Builder(this)
        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        builder.setView(progressBar)
        builder.setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog?.show()
    }

    private fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
    }

}
