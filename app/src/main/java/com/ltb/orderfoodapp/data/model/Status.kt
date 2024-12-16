package com.ltb.orderfoodapp.data.model

enum class Status(val id: Int, val description: String) {
    PENDING(0, "Chờ xác nhận"),
    PROCESSING(1, "Đang xử lý"),
    PACKAGING(2, "Đang đóng gói"),
    SHIPPED(3, "Đang giao hàng"),
    DELIVERING(4,"Đang được giao"),
    DELIVERED(5, "Đã giao hàng"),
    COMPLETED(6, "Đã hoàn thành"),
    CANCELLED(7, "Đã hủy"),
    REFUNDED(8, "Đã hoàn tiền"),
    PAYMENT_SUCCESS(9, "Đã thanh toán"),
    PAYMENT_FAILED(10,"Thanh toán thất bại");

    companion object {
        fun fromId(id: Int): Status? {
            return entries.find { it.id == id }
        }
    }
}