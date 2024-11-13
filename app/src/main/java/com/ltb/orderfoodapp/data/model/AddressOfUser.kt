package com.ltb.orderfoodapp.data.model

import com.google.firebase.firestore.DocumentReference

class AddressOfUser (
    var id_address : String,
    var addressType : AddressType,
    var infor : String,
    var user_id : DocumentReference,

)