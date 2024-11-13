package com.ltb.orderfoodapp.data.model
import com.google.firebase.firestore.DocumentReference
data class User (
    var id_user : String,
    var fullName : String,
    var email : String,
    var phoneNumber : Number,
    var bioInfor : String,
    var account_id :DocumentReference
)