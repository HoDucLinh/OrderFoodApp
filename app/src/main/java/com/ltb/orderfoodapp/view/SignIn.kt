@file:Suppress("DEPRECATION")

package com.ltb.orderfoodapp.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
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
    private lateinit var signInGoogle : ImageView
    private lateinit var signInFacebook :ImageView
    private lateinit var signInGithub : ImageView

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
        signInFacebook = findViewById<ImageView>(R.id.facebook)
        signInGoogle = findViewById(R.id.google)
        signInGithub = findViewById<ImageView>(R.id.github)

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



        var isProcessing = false

        loginBtn.setOnClickListener {
            if (isProcessing) {
                Toast.makeText(this, "Processing, please wait...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userNameLogin = userNameInput.editText?.text.toString()
            val passwordLogin = passwordInput.editText?.text.toString()

            if (userNameLogin.isNotEmpty() && passwordLogin.isNotEmpty()) {
                if (isValidEmail(userNameLogin)) {
                    isProcessing = true
                    loginBtn.isEnabled = false

                    val handler = Handler()
                    handler.postDelayed({
                        if (isProcessing) {
                            Toast.makeText(this, "Network is weak, please wait...", Toast.LENGTH_LONG).show()
                        }
                    }, 2000)

                    auth.authEmail(userNameLogin, passwordLogin) { success, message ->
                        isProcessing = false
                        loginBtn.isEnabled = true
                        handler.removeCallbacksAndMessages(null)
                    }
                } else {
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
        signInFacebook.setOnClickListener {
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


    private var isProcessingGoogle = false  // Biến để theo dõi quá trình đăng nhập Google

    private fun signIn() {
        // Kiểm tra nếu đang trong quá trình xử lý
        if (isProcessingGoogle) {
            Toast.makeText(this, "Processing, please wait...", Toast.LENGTH_SHORT).show()
            return
        }

        // Đánh dấu đang xử lý
        isProcessingGoogle = true
        signInGoogle.isEnabled = false
        // Lấy intent đăng nhập Google
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

        // Hiển thị thông báo nếu mạng yếu sau 2 giây
        val handler = Handler()
        handler.postDelayed({
            if (isProcessingGoogle) {
                Toast.makeText(this, "Network is weak, please wait...", Toast.LENGTH_LONG).show()
            }
        }, 2000)  // 2 giây sau sẽ hiển thị cảnh báo nếu không có phản hồi
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Lấy tài khoản Google đã đăng nhập
                val account = task.getResult(ApiException::class.java)!!
                // Gọi phương thức để xác thực tài khoản Google với Firebase
                auth.firebaseAuthWithGoogle(account.idToken!!)

                // Sau khi xử lý xong, đánh dấu không còn đang xử lý và kích hoạt lại nút
                isProcessingGoogle = false
                signInGoogle.isEnabled = true  // Enable nút Google sau khi xử lý xong
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                // Nếu đăng nhập thất bại, đánh dấu không còn đang xử lý và kích hoạt lại nút
                isProcessingGoogle = false
                signInGoogle.isEnabled = true  // Enable nút Google sau khi xử lý xong
            }
        }
    }


    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}
