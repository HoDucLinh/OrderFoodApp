package com.ltb.orderfoodapp.data.model

import java.util.Date

class ReviewOrder (
    var idReviewOrder: Int,
    var rating : Float,
    var comment : String,
    var reviewDate : Date,
    var orderId : Int
){
    constructor() : this(0,0f,"", Date(), 0)
}