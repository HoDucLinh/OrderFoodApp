package com.ltb.orderfoodapp.data.model

data class Restaurant(
    var id_restaurant: String,
    var name : String,
    var address : String
){
    constructor() : this("","","")
}