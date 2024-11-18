package com.ltb.orderfoodapp.data.model

class Status (
    var idStatus : Int,
    var statusName : String,
    var description : String
)
{
    constructor() : this(0, "", "")
}