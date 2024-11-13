package com.ltb.orderfoodapp.data.model
import com.google.firebase.firestore.DocumentReference
class Role (
    var id_role : String,
    var roleName : String,
    var user_id : DocumentReference
)