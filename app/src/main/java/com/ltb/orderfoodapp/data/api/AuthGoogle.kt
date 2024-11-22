//package com.ltb.orderfoodapp.data.api
//
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.auth.auth
//import com.google.firebase.Firebase
//import com.ltb.orderfoodapp.R
//
//
///**
// * Demonstrate Firebase Authentication using a Google ID Token.
// */
//class AuthGoogle {
//    private lateinit var auth: FirebaseAuth
//    private lateinit var googleSignInClient: GoogleSignInClient
//
//    init {
//        auth = Firebase.auth
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(context, gso)
//    }
//
//    fun signIn(context: Context, callback: (FirebaseUser?) -> Unit) {
//        val signInIntent = googleSignInClient.signInIntent
//        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
//        // ...
//    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, callback: (FirebaseUser?) -> Unit) {
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)!!
//                firebaseAuthWithGoogle(account.idToken!!, callback)
//            } catch (e: ApiException) {
//                callback(null)
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String, callback: (FirebaseUser?) -> Unit) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    callback(auth.currentUser)
//                } else {
//                    callback(null)
//                }
//            }
//    }
//
//    companion object {
//        private const val TAG = "GoogleAuth"
//        const val RC_SIGN_IN = 9001
//    }
//}
