package com.ltb.orderfoodapp.data.api

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.data.dao.CartDAO
import com.ltb.orderfoodapp.data.dao.RoleDAO
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.Role
import com.ltb.orderfoodapp.data.model.User
import com.ltb.orderfoodapp.view.AdminDashboardHome
import com.ltb.orderfoodapp.view.EditRole
import com.ltb.orderfoodapp.view.Home
import com.ltb.orderfoodapp.view.SellerDashboardHome
import com.ltb.orderfoodapp.view.SignIn
import kotlin.math.truncate

class AuthManager(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userDAO: UserDAO

    fun checkCurrentUser(onUserSignedIn: (FirebaseUser?) -> Unit) {
        val currentUser = auth.currentUser
        onUserSignedIn(currentUser)
    }
    init {


        userDAO = UserDAO(context)
    }

    fun authEmail(email: String, password: String ,callback: (Boolean, String) -> Unit ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkAdmin(email,password)
                } else {
                   callback(false, "Login error")
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
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
    }

//    fun deleteUser() {
//        val user = Firebase.auth.currentUser!!
//        user.delete()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "User account deleted.")
//                }
//            }
//        // [END delete_user]
//    }

    fun firebaseAuthWithGoogle(idToken: String, callback: (Boolean, String) -> Unit ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                checkAdmin(user?.email.toString(),"")
                updateUI(user)
            } else {
                callback(false,"Login Error")
                updateUI(null)
            }
        }
    }
    fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }

    fun sendPasswordReset(emailAddress : String) {
        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    fun checkAdmin(email: String, password: String) {

        var user = userDAO.getUser(email, password)
        if(user == null){
            val newUser = User(email = email, password = password)
            val cartDAO = CartDAO(context)
            val userID = userDAO.addUser(newUser)
            val cartID = cartDAO.insertCart(0, userID)
            userDAO.updateUserCartId(userID, cartID.toInt())
            user = userDAO.getUser(email, password)
        }
        val roleId = user?.getRoleId() ?: 2
        val roleEnum = Role.fromRoleId(roleId)
        println( "RoleID " + roleId + "Role Enum " +  roleEnum)
        if (user != null) {
            when (roleEnum) {
                Role.RESTAURANT -> {
                    println("CardID " + user.getCartId())
                    saveLoginStatus(true, "restaurant", user)
                    val restaurantHomePage = Intent(context, SellerDashboardHome::class.java)
                    Toast.makeText(context, "Login success, hello ${user.getFullName()}", Toast.LENGTH_SHORT).show()
                    context.startActivity(restaurantHomePage)
                }
                Role.CUSTOMER -> {
                    println("CardID " + user.getCartId())
                    saveLoginStatus(true, "customer", user)
                    val userHomePage = Intent(context, Home::class.java)
                    Toast.makeText(context, "Login success, hello ${user.getFullName()}", Toast.LENGTH_SHORT).show()
                    context.startActivity(userHomePage)
                }
                Role.ADMIN -> {
                    println("CardID " + user.getCartId())
                    saveLoginStatus(true, "admin", user)
                    val  adminHomePage= Intent(context, AdminDashboardHome::class.java)
                    Toast.makeText(context, "Login success, hello ${user.getFullName()}", Toast.LENGTH_SHORT).show()
                    context.startActivity(adminHomePage)
                }
                else -> {
                    // Xử lý khi không có role hợp lệ
                    println("Invalid roleId: $roleId")
                }
            }
        }
    }



    fun saveLoginStatus(isLoggedIn: Boolean, role: String, user: User) {
        val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("role", role)
        editor.putString("user", Gson().toJson(user))
        editor.apply()
    }
    fun logout() {
        val sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        val loginIntent = Intent(context, SignIn::class.java)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(loginIntent)

    }
    companion object {
        const val RC_SIGN_IN = 9001
        fun createInstance(context: Context): AuthManager {
            return AuthManager(context)
        }
    }
}
