package com.ltb.orderfoodapp.data.model

class ProductCart(
    private var idProductCart: Int = 0,
    private var productId: Int,
    private var cartId: Int,
    private var quantity: Int,
    private var name: String,
    private var price: Int,
    private var rating: Float = 0f,
    var images: MutableList<String> = mutableListOf(),
) {
    // Constructor không tham số
    constructor() : this(0, 0, 0, 0, "", 0)

    // Getter và Setter cho idProductCart
    fun getIdProductCart(): Int = idProductCart
    fun setIdProductCart(idProductCart: Int) {
        this.idProductCart = idProductCart
    }

    // Getter và Setter cho productId
    fun getProductId(): Int = productId
    fun setProductId(productId: Int) {
        this.productId = productId
    }

    // Getter và Setter cho cartId
    fun getCartId(): Int = cartId
    fun setCartId(cartId: Int) {
        this.cartId = cartId
    }

    // Getter và Setter cho quantity
    fun getQuantity(): Int = quantity
    fun setQuantity(quantity: Int) {
        this.quantity = quantity
    }

    // Getter và Setter cho name
    fun getName(): String = name
    fun setName(name: String) {
        this.name = name
    }

    // Getter và Setter cho price
    fun getPrice(): Int = price
    fun setPrice(price: Int) {
        this.price = price
    }

    // Getter và Setter cho rating
    fun getRating(): Float = rating
    fun setRating(rating: Float) {
        this.rating = rating
    }
}
