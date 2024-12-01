@file:Suppress("DEPRECATION")

package com.ltb.orderfoodapp.view


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
    public override fun onCreate(savedInstanceState: Bundle?) {
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

        //Lay userName, password de login
        loginBtn.setOnClickListener{
            val userNameLogin = findViewById<TextInputLayout>(R.id.userNameLogin).editText?.text.toString()
            val passwordLogin = findViewById<TextInputLayout>(R.id.passwordLogin).editText?.text.toString()
            if(userNameLogin!=""&&passwordLogin!=""){
                auth.authEmail(userNameLogin,passwordLogin)
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
        sigInFacebook.setOnClickListener{
//                AuthFacebook.login(this) { user ->
//                    if (user != null) {
//                        // Đăng nhập thành công
//                    } else {
//                        // Đăng nhập thất bại
//                    }
//                }
            }
        signInGoogle.setOnClickListener{
            signIn()
        }
    }
    //sign in with gooogle
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                auth.firebaseAuthWithGoogle(account.idToken!!)
                Toast.makeText(this, "Success, Hello${account.displayName}", Toast.LENGTH_SHORT).show()

            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }



}