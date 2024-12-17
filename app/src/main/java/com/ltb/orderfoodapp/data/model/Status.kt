package com.ltb.orderfoodapp.data.model

enum class Status(val id: Int, val description: String) {
    PROCESSING(1, "Đang xử lý"),
    PACKAGING(2, "Đang đóng gói"),
    SHIPPED(3, "Đang giao hàng"),
    DELIVERED(4, "Đã giao hàng");

    companion object {
        fun fromId(id: Int): Status? {
            return entries.find { it.id == id }
        }
    }
}