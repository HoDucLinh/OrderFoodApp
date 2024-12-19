package com.ltb.orderfoodapp.data.model

data class Product(
    private var idProduct: Int = 0,
    private var name: String = "",
    private var price: Int = 0,
    private var rating: Float = 0f,
    private var description: String = "",
    private var category: String = "",
    private var images: MutableList<String> = mutableListOf()
) {
    fun getIdProduct(): Int {
        return idProduct
    }

    fun setIdProduct(value: Int) {
        idProduct = value
    }

    fun getName(): String {
        return name
    }

    fun setName(value: String) {
        name = value
    }

    fun getPrice(): Int {
        return price
    }

    fun setPrice(value: Int) {
        price = value
    }

    fun getRating(): Float {
        return (rating * 10).toInt() / 10f
    }

    fun setRating(value: Float) {
        rating = value
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(value: String) {
        description = value
    }

//    fun getRestaurant(): String {
//        return restaurant
//    }
//
//    fun setRestaurant(value: String) {
//        restaurant = value
//    }

    fun getCategory(): String {
        return category
    }

    fun setCategory(value: String) {
        category = value
    }

    fun getImages(): MutableList<String> {
        return images
    }

    fun setImages(value: MutableList<String>) {
        images = value
    }
}
