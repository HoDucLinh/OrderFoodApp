package com.ltb.orderfoodapp.data.api

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ltb.orderfoodapp.view.Home
class AuthEmail(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    public fun checkCurrentUser(onUserSignedIn: (FirebaseUser?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            onUserSignedIn(currentUser)
        } else {
            onUserSignedIn(null)
        }
    }

    public fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val homePage = Intent(context, Home::class.java)
                    context.startActivity(homePage)
                } else {
                    Toast.makeText(context, "Login Error.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    public fun sendEmailVerification(onResult: (Boolean) -> Unit) {
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


    /**
     * Cập nhật giao diện người dùng.
     */
    private fun updateUI(user: FirebaseUser?) {
        // Thực hiện cập nhật giao diện nếu cần.
    }

    companion object {
        private const val TAG = "EmailPassword"
        fun createInstance(context: Context): AuthEmail {
                return AuthEmail(context)
            }
    }
}