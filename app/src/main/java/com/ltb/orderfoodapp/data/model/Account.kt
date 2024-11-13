package com.ltb.orderfoodapp.data.model

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.credentials.PasswordCredential
import com.google.firebase.firestore.DocumentReference

data class Account(
    var id_account : String,
    var email: String,
    var password : String,
    var user_id : DocumentReference
)