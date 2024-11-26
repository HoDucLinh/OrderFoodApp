package com.ltb.orderfoodapp.data.model

class User (
    var idUser : Int = 0,
    var fullName : String = "",
    var email : String = "",
    var phoneNumber : String = "",
    var bioInfor : String = "",
    var accountId : Int = 0,
    var password : String = "",
    var cartId : Int = 0,
    var addresses: MutableList<AddressOfUser> = mutableListOf(),
    var roles: Role = Role.CUSTOMER
)
{
}