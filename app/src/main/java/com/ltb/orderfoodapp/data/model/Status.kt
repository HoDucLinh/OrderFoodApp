package com.ltb.orderfoodapp.data.model

enum class Status(val id: Int, val description: String) {
    PENDING(1, "Pending approval"),
    PROCESSING(2, "Order is being processed"),
    DELIVERED(3, "Order delivered");
    companion object {
        fun fromId(id: Int): Status? {
            return values().find { it.id == id }
        }
    }
}
