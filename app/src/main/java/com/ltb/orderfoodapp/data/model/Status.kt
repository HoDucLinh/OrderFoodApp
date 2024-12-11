package com.ltb.orderfoodapp.data.model

enum class Status(val id: Int, val description: String) {
    PROCESSING(1, "Order is being processed"),
    DELIVERED(2, "Order delivered");
    companion object {
        fun fromId(id: Int): Status? {
            return values().find { it.id == id }
        }
    }
}
