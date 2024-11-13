package com.ltb.orderfoodapp.data.model

import com.google.firebase.firestore.DocumentReference

class OrderDetail (
    var id_orderDetail : String,
    var order_id : DocumentReference,
    var product_id : DocumentReference,
    var quantity : Number,
    var unitPrice : Number,
    var totalPrice : Number
)