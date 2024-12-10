package com.ltb.orderfoodapp.data.model

class Image(
    private var idImage: Int = 0,
    private var value: String = "",
    private var productId: Int = 0
) {

    fun getIdImage(): Int {
        return idImage
    }

    fun setIdImage(value: Int) {
        idImage = value
    }

    fun getValue(): String {
        return value
    }

    fun setValue(value: String) {
        this.value = value
    }

    fun getProductId(): Int {
        return productId
    }

    fun setProductId(value: Int) {
        productId = value
    }
}
