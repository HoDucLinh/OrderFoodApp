package com.ltb.orderfoodapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ltb.orderfoodapp.data.DatabaseHelper
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Image
import com.ltb.orderfoodapp.data.model.Product

class ProductDAO(context: Context) {
    private val db: SQLiteDatabase = DatabaseHelper.getInstance(context).writableDatabase
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var imageDAO: ImageDAO
    private lateinit var restaurantDAO: RestaurantDAO

    init {
        categoryDAO = CategoryDAO(context)
        restaurantDAO = RestaurantDAO()
//        demo()

    }


    fun close() {
        db.close()

    }

    fun demo() {
        val products = mutableListOf<Product>()

        val sampleProducts = listOf(
            Product(
                name = "Pizza Margherita",
                price = 150000,
                rating = 4.5f,
                description = "Pizza với phô mai và cà chua tươi",
                restaurant = "Pizza Hut",
                category = "Pizza",
                images = mutableListOf(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg"
                )
            ),
            Product(
                name = "Hamburger Classic",
                price = 120000,
                rating = 4.0f,
                description = "Hamburger với thịt bò và rau tươi",
                restaurant = "Burger King",
                category = "Hamburger",
                images = mutableListOf(
                    "https://example.com/image3.jpg",
                    "https://example.com/image4.jpg"
                )
            ),
            Product(
                name = "Sushi Combo",
                price = 200000,
                rating = 4.8f,
                description = "Combo sushi với cá hồi và tôm",
                restaurant = "Sushi House",
                category = "Sushi",
                images = mutableListOf(
                    "https://example.com/image5.jpg",
                    "https://example.com/image6.jpg"
                )
            ),
            Product(
                name = "Bánh Mì Thịt",
                price = 30000,
                rating = 4.2f,
                description = "Bánh mì kẹp thịt truyền thống Việt Nam",
                restaurant = "Bánh Mì Huỳnh Hoa",
                category = "Bánh Mì",
                images = mutableListOf(
                    "https://example.com/image7.jpg",
                    "https://example.com/image8.jpg"
                )
            ),
            Product(
                name = "Phở Bò",
                price = 45000,
                rating = 4.7f,
                description = "Phở bò truyền thống với nước dùng đậm đà",
                restaurant = "Phở 24",
                category = "Phở",
                images = mutableListOf(
                    "https://example.com/image9.jpg",
                    "https://example.com/image10.jpg"
                )
            ),
            Product(
                name = "Cơm Tấm Sườn",
                price = 50000,
                rating = 4.3f,
                description = "Cơm tấm sườn bì chả",
                restaurant = "Cơm Tấm Ba Ghiền",
                category = "Cơm Tấm",
                images = mutableListOf(
                    "https://example.com/image11.jpg",
                    "https://example.com/image12.jpg"
                )
            ),
            Product(
                name = "Bún Bò Huế",
                price = 55000,
                rating = 4.6f,
                description = "Món bún bò đậm vị từ miền Trung",
                restaurant = "Bún Bò O Nga",
                category = "Bún Bò",
                images = mutableListOf(
                    "https://example.com/image13.jpg",
                    "https://example.com/image14.jpg"
                )
            ),
            Product(
                name = "Tacos Mexico",
                price = 180000,
                rating = 4.4f,
                description = "Tacos truyền thống với thịt bò",
                restaurant = "Taco Bell",
                category = "Tacos",
                images = mutableListOf(
                    "https://example.com/image15.jpg",
                    "https://example.com/image16.jpg"
                )
            ),
            Product(
                name = "Trà Sữa Trân Châu",
                price = 40000,
                rating = 4.9f,
                description = "Trà sữa hương vị truyền thống với trân châu đen",
                restaurant = "Gong Cha",
                category = "Đồ Uống",
                images = mutableListOf(
                    "https://example.com/image17.jpg",
                    "https://example.com/image18.jpg"
                )
            ),
            Product(
                name = "Salad Caesar",
                price = 70000,
                rating = 4.5f,
                description = "Salad Caesar với sốt kem và gà nướng",
                restaurant = "The Green Table",
                category = "Salad",
                images = mutableListOf(
                    "https://example.com/image19.jpg",
                    "https://example.com/image20.jpg"
                )
            )
        )

        products.addAll(sampleProducts)
    }


    fun addProduct(product: Product): Long {
//        if (product.name.isEmpty() || product.price <= 0 || product.rating < 0 || product.description.isEmpty()) {
//            throw IllegalArgumentException("Invalid product data")
//        }
        return try {
            val productId = insertProduct(product)

            val categoryId = getOrInsertCategory(product.category)
            updateProductCategory(productId, categoryId)

            val restaurantId = getOrInsertRestaurant(product.restaurant)
            updateProductRestaurant(productId, restaurantId)

            // Thêm hình ảnh vào sản phẩm
            if (product.images.isNotEmpty()) {
                addImagesToProduct(productId, product.images)
            }

            productId
        } catch (e: Exception) {
            throw RuntimeException("Failed to add product", e)
        }
    }

    // Thêm sản phẩm vào cơ sở dữ liệu
    private fun insertProduct(product: Product): Long {
        val values = ContentValues().apply {
            put("Name", product.name)
            put("Price", product.price)
            put("Rating", product.rating)
            put("Description", product.description)
        }
        return db.insert("Product", null, values)
    }

    // Kiểm tra xem danh mục có tồn tại chưa, nếu chưa thì thêm mới
    private fun getOrInsertCategory(categoryName: String): Int {
        var categoryId = categoryDAO.getCategoryIdByName(categoryName)
        if (categoryId == -1) {
            val categoryValues = ContentValues().apply {
                put("Name", categoryName)
                put("Description", "dfasdf")
            }
            val newCategoryId = db.insert("Category", null, categoryValues)
            categoryId = newCategoryId.toInt()
        }
        return categoryId
    }

    // Cập nhật sản phẩm với category_id
    private fun updateProductCategory(productId: Long, categoryId: Int) {
        val updateCategoryValues = ContentValues().apply {
            put("Category_ID", categoryId)
        }
        db.update("Product", updateCategoryValues, "ID = ?", arrayOf(productId.toString()))
    }

    // Kiểm tra xem nhà hàng có tồn tại chưa, nếu chưa thì thêm mới
    private fun getOrInsertRestaurant(restaurantName: String): Int {
        var restaurantId = restaurantDAO.getRestaurantIdByName(restaurantName)
        if (restaurantId == -1) {
            val restaurantValues = ContentValues().apply {
                put("Name", restaurantName)
                put("Address", " ")
            }
            val newRestaurantId = db.insert("Restaurant", null, restaurantValues)
            restaurantId = newRestaurantId.toInt()
        }
        return restaurantId
    }

    // Cập nhật sản phẩm với restaurant_id
    private fun updateProductRestaurant(productId: Long, restaurantId: Int) {
        val updateRestaurantValues = ContentValues().apply {
            put("Restaurant_ID", restaurantId)
        }
        db.update("Product", updateRestaurantValues, "ID = ?", arrayOf(productId.toString()))
    }

    // Thêm hình ảnh cho sản phẩm
    private fun addImagesToProduct(productId: Long, images: List<String>) {
        val imageDAO = ImageDAO(db)
        images.forEach { imageUrl ->
            try {
                imageDAO.addImage(imageUrl, productId.toInt())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllProducts(): MutableList<Product> {
        val productList = mutableListOf<Product>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                """ 
            SELECT 
                p.ID, p.Name, p.Price, p.Rating, p.Description, 
                c.Name AS CategoryName, r.Name AS RestaurantName, i.Value AS ImageSource 
            FROM Product p
            LEFT JOIN Category c ON p.Category_ID = c.ID
            LEFT JOIN Image i ON p.ID = i.Product_ID
            LEFT JOIN Restaurant r ON p.Restaurant_ID = r.ID
            """, null
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val product = Product(
                        idProduct = it.getInt(it.getColumnIndexOrThrow("ID")),
                        name = it.getString(it.getColumnIndexOrThrow("Name")),
                        price = it.getInt(it.getColumnIndexOrThrow("Price")),
                        rating = it.getFloat(it.getColumnIndexOrThrow("Rating")),
                        description = it.getString(it.getColumnIndexOrThrow("Description")),
                    )
                    val restaurant = it.getString(it.getColumnIndexOrThrow("RestaurantName"))
                    product.restaurant = restaurant
                    val categoryName = it.getString(it.getColumnIndexOrThrow("CategoryName"))
                    product.category = categoryName
                    val imageUrl = it.getString(it.getColumnIndexOrThrow("ImageSource"))
                    if (imageUrl != null) {
                        product.images.add(imageUrl)
                    }

                    productList.add(product)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close() // Đảm bảo đóng cursor khi không còn sử dụng
        }
        return productList
    }


//
//    // Cập nhật sản phẩm
//    fun updateProduct(product: Product): Int {
//        val values = ContentValues().apply {
//            put("name", product.name)
//            put("storeName", product.storeName)
//            put("price", product.price)
//            put("imageResource", product.imageResource)
//            put("rating", product.rating)
//            put("category", product.category)
//            put("description", product.description)
//        }
//        val selection = "name = ?"
//        val selectionArgs = arrayOf(product.name)
//        return db.update("Product", values, selection, selectionArgs)
//    }
//
//    // Xóa sản phẩm
//    fun deleteProduct(name: String): Int {
//        val selection = "name = ?"
//        val selectionArgs = arrayOf(name)
//        return db.delete("Product", selection, selectionArgs)
//    }
}
