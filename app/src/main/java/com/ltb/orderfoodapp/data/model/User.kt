package com.ltb.orderfoodapp.data.model

class User(
    private var idUser: Int = 0,
    private var fullName: String = "",
    private var email: String = "",
    private var phoneNumber: String = "",
    private var bioInfor: String = "",
    private var password: String = "",
    private var cartId: Int = 0,
    var addresses: MutableList<AddressOfUser> = mutableListOf(),
    private var roleId: Int = 2
) {
    // Getter và Setter cho idUser
    fun getIdUser(): Int = idUser
    fun setIdUser(idUser: Int) {
        this.idUser = idUser
    }

    // Getter và Setter cho fullName
    fun getFullName(): String = fullName
    fun setFullName(fullName: String) {
        this.fullName = fullName
    }

    // Getter và Setter cho email
    fun getEmail(): String = email
    fun setEmail(email: String) {
        this.email = email
    }

    // Getter và Setter cho phoneNumber
    fun getPhoneNumber(): String = phoneNumber
    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    // Getter và Setter cho bioInfor
    fun getBioInfor(): String = bioInfor
    fun setBioInfor(bioInfor: String) {
        this.bioInfor = bioInfor
    }

    // Getter và Setter cho password
    fun getPassword(): String = password
    fun setPassword(password: String) {
        this.password = password
    }

    // Getter và Setter cho cartId
    fun getCartId(): Int = cartId
    fun setCartId(cartId: Int) {
        this.cartId = cartId
    }

    // Getter và Setter cho roleId
    fun getRoleId(): Int = roleId
    fun setRoleId(roleId: Int) {
        this.roleId = roleId
    }
}
