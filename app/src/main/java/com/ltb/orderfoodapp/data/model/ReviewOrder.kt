package com.ltb.orderfoodapp.data.model

import com.google.firebase.firestore.DocumentReference
import java.time.LocalDate

class ReviewOrder (
    var id_reviewOrder : String,
    var rating : Number,
    var comment : String,
    var reviewDate : LocalDate,
    var order_id : DocumentReference
)