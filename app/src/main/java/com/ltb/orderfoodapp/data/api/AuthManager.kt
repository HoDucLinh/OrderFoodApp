package com.ltb.orderfoodapp.data.api

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.view.Home
import com.ltb.orderfoodapp.view.SellerDashboardHome

class AuthManager(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    // Kiểm tra người dùng hiện tại
    fun checkCurrentUser(onUserSignedIn: (FirebaseUser?) -> Unit) {
        val currentUser = auth.currentUser
        onUserSignedIn(currentUser)
    }

    // Đăng nhập bằng email và mật khẩu
    fun authEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkAdmin(email,password)
                } else {
                    Toast.makeText(context, "Login Error.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                updateUI(null)
            }
        }
    }


    // Gửi email xác minh
    fun sendEmailVerification(onResult: (Boolean) -> Unit) {
        val user = auth.currentUser
        if (user != null) {
            user.sendEmailVerification()
                .addOnCompleteListener { task ->
                    onResult(task.isSuccessful)
                }
        } else {
            onResult(false)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
    }
    fun checkAdmin(userName: String?, password: String) {
        if (userName == "admin@gmail.com" && password == "123456") {
            val adminHomePage = Intent(context, SellerDashboardHome::class.java)
            context.startActivity(adminHomePage)
        } else {
            val userHomePage = Intent(context, Home::class.java)
            context.startActivity(userHomePage)
        }
    }

    fun saveLoginStatus(isLoggedIn: Boolean, userId: String?, role : String?) {
        val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    companion object {
        const val RC_SIGN_IN = 9001
        fun createInstance(context: Context): AuthManager {
            return AuthManager(context)
        }
    }
}
