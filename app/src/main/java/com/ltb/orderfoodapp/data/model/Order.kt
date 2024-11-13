package com.ltb.orderfoodapp.data.model

import com.google.firebase.firestore.DocumentReference
import java.time.LocalDate

class Order (
    var id_order: String,
    var totalAmount : Number,
    var orderStatus : DocumentReference,
    var orderDate : LocalDate,
    var user_id : DocumentReference,
    var restaurant_id: DocumentReference
)