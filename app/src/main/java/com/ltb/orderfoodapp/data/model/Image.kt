package com.ltb.orderfoodapp.data.model


data class Image(
    var id_image: String,
    var value : MutableList<String>
){
    constructor() : this("", mutableListOf())
}