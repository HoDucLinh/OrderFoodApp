package com.ltb.orderfoodapp.data.model

enum class Status(val id: Int, val description: String) {
    PROCESSING(1, "Đang xử lý"),
    SHIPPED(2, "Đang giao hàng"),
    DELIVERED(3, "Đã giao hàng");

    companion object {
        fun fromId(id: Int): Status? {
            return entries.find { it.id == id }
        }
    }
}