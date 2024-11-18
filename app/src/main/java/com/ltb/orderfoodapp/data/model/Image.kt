package com.ltb.orderfoodapp.data.model

class Image (
    var idImage : Int,
    var value : String,
    var productId: Int,
)
{
    constructor() : this (0, "" , 0 )
}