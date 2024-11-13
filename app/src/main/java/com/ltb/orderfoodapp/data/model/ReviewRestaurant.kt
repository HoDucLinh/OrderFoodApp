package com.ltb.orderfoodapp.data.model

import com.google.firebase.firestore.DocumentReference
import java.time.LocalDate

class ReviewRestaurant (
    var id_reviewRestaurant : String,
    var rating : Number,
    var comment : String,
    var reviewDate : LocalDate,
    var restaurant_id : DocumentReference
)