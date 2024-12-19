package com.ltb.orderfoodapp.data.model


enum class Role(val roleId: Int) {
    ADMIN(1),
    CUSTOMER(2),
    RESTAURANT(3);

    companion object {
        fun fromRoleId(roleId: Int): Role? {
            return Role.values().find { it.roleId == roleId }
        }
    }
}
