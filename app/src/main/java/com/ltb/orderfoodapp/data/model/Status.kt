package com.ltb.orderfoodapp.data.model

class Status(
    private var idStatus: Int,
    private var statusName: String,
    private var description: String
) {
    // Constructor không tham số
    constructor() : this(0, "", "")

    // Getter và Setter cho idStatus
    fun getIdStatus(): Int = idStatus
    fun setIdStatus(idStatus: Int) {
        this.idStatus = idStatus
    }

    // Getter và Setter cho statusName
    fun getStatusName(): String = statusName
    fun setStatusName(statusName: String) {
        this.statusName = statusName
    }

    // Getter và Setter cho description
    fun getDescription(): String = description
    fun setDescription(description: String) {
        this.description = description
    }
}
