package com.ltb.orderfoodapp.data.model

import com.google.firebase.firestore.DocumentReference

class OrderStatus (
    var id_orderStatus : String,
    var order_id : DocumentReference,
    var status_id : DocumentReference
)