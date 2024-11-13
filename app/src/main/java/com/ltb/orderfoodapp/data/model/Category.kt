package com.ltb.orderfoodapp.data.model

data class Category (
        var id_category : String,
        val name: String,
        var description : String
){
        constructor() : this("","","")
}