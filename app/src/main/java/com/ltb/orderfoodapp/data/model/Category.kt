package com.ltb.orderfoodapp.data.model

// Category
class Category(
        private var idCategory: Int = 0,
        private var name: String = "",
        private var description: String = "",
        private var product: MutableList<Product> = mutableListOf()
) {
        // Getter và Setter cho idCategory
        fun getIdCategory(): Int {
                return idCategory
        }

        fun setIdCategory(value: Int) {
                idCategory = value
        }

        // Getter và Setter cho name
        fun getName(): String {
                return name
        }

        fun setName(value: String) {
                name = value
        }

        // Getter và Setter cho description
        fun getDescription(): String {
                return description
        }

        fun setDescription(value: String) {
                description = value
        }

        fun getProduct(): MutableList<Product> {
                return product
        }

        fun setProduct(value: MutableList<Product>) {
                product = value
        }
}
