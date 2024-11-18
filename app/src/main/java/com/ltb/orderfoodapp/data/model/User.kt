package com.ltb.orderfoodapp.data.model

class User (
    var idUser : Int,
    var fullName : String,
    var email : String,
    var phoneNumber : String,
    var bioInfor : String,
    var accountId : Int,
    var cartId : Int,
    var addresses: MutableList<AddressOfUser>,
    var roles: MutableList<Role>
)
{
    constructor() : this(0,"", "","","",0 ,0, mutableListOf(), mutableListOf())
}