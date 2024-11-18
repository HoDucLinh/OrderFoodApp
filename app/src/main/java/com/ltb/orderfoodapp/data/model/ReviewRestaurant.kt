package com.ltb.orderfoodapp.data.model

import java.util.Date

class ReviewRestaurant (
    var idReviewRestaurant : Int,
    var rating : Float ,
    var comment : String,
    var reviewDate : Date,
    var restaurantId : Int,
){
    constructor() : this(0,0f, "",Date(), 0)
}