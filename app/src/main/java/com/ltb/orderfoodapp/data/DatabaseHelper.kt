package com.ltb.orderfoodapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.ltb.orderfoodapp.data.dao.CategoryDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.Category
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.User

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var db: SQLiteDatabase? = null

    companion object {
        private const val DATABASE_NAME = "oderfoodapp.db"
        private const val DATABASE_VERSION = 2

        // Tên bảng và các cột
        private const val TABLE_CATEGORY = "Category"
        private const val TABLE_PRODUCT = "Product"
        private const val TABLE_IMAGE = "Image"
//        private const val TABLE_ADDRESS_USER = "AddressOfUser"
//        private const val TABLE_RESTAURANT = "Restaurant"
        private const val TABLE_USER = "User"
        private const val TABLE_ROLE = "Role"
        private const val TABLE_ORDER = "\"Order\""

        //        private const val TABLE_ORDER_STATUS = "OrderStatus"
//        private const val TABLE_STATUS = "Status"
        private const val TABLE_ORDER_DETAIL = "OrderDetail"
        private const val TABLE_REVIEW_ORDER = "ReviewOrder"
//        private const val TABLE_REVIEW_RESTAURANT = "ReviewRestaurant"
        private const val TABLE_CART = "Cart"
        private const val TABLE_PRODUCT_CART = "Product_Cart"

        private var instance: DatabaseHelper? = null
        fun getInstance(context: Context): DatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tạo các bảng
        db.execSQL(CREATE_CATEGORY_TABLE)
        db.execSQL(CREATE_PRODUCT_TABLE)
        db.execSQL(CREATE_IMAGE_TABLE)
//        db.execSQL(CREATE_ADDRESS_USER_TABLE)
//        db.execSQL(CREATE_RESTAURANT_TABLE)
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_ROLE_TABLE)
        db.execSQL(CREATE_ORDER_TABLE)
//        db.execSQL(CREATE_ORDER_STATUS_TABLE)
//        db.execSQL(CREATE_STATUS_TABLE)
        db.execSQL(CREATE_ORDER_DETAIL_TABLE)
        db.execSQL(CREATE_REVIEW_ORDER_TABLE)
//        db.execSQL(CREATE_REVIEW_RESTAURANT_TABLE)
        db.execSQL(CREATE_CART_TABLE)
        db.execSQL(CREATE_PRODUCT_CART_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_IMAGE")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADDRESS_USER")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESTAURANT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROLE")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER_STATUS")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_STATUS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER_DETAIL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEW_ORDER")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEW_RESTAURANT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT_CART")
        onCreate(db)
        initData()
    }

    // Tạo các bảng
    private val CREATE_CATEGORY_TABLE = """
        CREATE TABLE $TABLE_CATEGORY (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            Name TEXT NOT NULL,
            Description TEXT
        )
    """.trimIndent()

    private val CREATE_PRODUCT_TABLE = """
        CREATE TABLE $TABLE_PRODUCT (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            Name TEXT NOT NULL,
            Price REAL NOT NULL,
            Rating REAL NOT NULL,
            Description TEXT,
            Category_ID INTEGER,
            FOREIGN KEY (Category_ID) REFERENCES $TABLE_CATEGORY(ID)
        )
    """.trimIndent()

    private val CREATE_IMAGE_TABLE = """
        CREATE TABLE $TABLE_IMAGE (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            Value TEXT NOT NULL,
            Product_ID INTEGER,
            FOREIGN KEY (Product_ID) REFERENCES $TABLE_PRODUCT(ID)
        )
    """.trimIndent()

//    private val CREATE_ADDRESS_USER_TABLE = """
//        CREATE TABLE $TABLE_ADDRESS_USER (
//            ID INTEGER PRIMARY KEY AUTOINCREMENT,
//            AddressType TEXT NOT NULL,
//            Info TEXT NOT NULL,
//            User_ID INTEGER,
//            FOREIGN KEY (User_ID) REFERENCES $TABLE_USER(ID)
//        )
//    """.trimIndent()
//    private val CREATE_RESTAURANT_TABLE = """
//        CREATE TABLE $TABLE_RESTAURANT (
//            ID INTEGER PRIMARY KEY AUTOINCREMENT,
//            Name TEXT NOT NULL,
//            Address TEXT NOT NULL
//        )
//    """.trimIndent()

    private val CREATE_USER_TABLE = """
        CREATE TABLE $TABLE_USER (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            FullName TEXT NOT NULL,
            Email TEXT NOT NULL,
            PhoneNumber TEXT NOT NULL,
            BioInfor TEXT,
            Password TEXT NOT NULL,
            Cart_ID INTEGER,
            Role_ID INTEGER,
            FOREIGN KEY (Cart_ID) REFERENCES $TABLE_CART(ID)
        )
    """.trimIndent()

    private val CREATE_ROLE_TABLE = """
        CREATE TABLE $TABLE_ROLE (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            RoleName TEXT NOT NULL
        )
    """.trimIndent()

    private val CREATE_ORDER_TABLE = """
        CREATE TABLE $TABLE_ORDER (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            totalAmount REAL NOT NULL,
            Status INTEGER NOT NULL,
            orderDate TEXT NOT NULL,
            User_ID INTEGER NOT NULL,
            FOREIGN KEY (User_ID) REFERENCES $TABLE_USER(ID)
        )
    """.trimIndent()

//    private val CREATE_ORDER_STATUS_TABLE = """
//        CREATE TABLE $TABLE_ORDER_STATUS (
//            ID INTEGER PRIMARY KEY AUTOINCREMENT,
//            Order_ID INTEGER,
//            Status_ID INTEGER,
//            FOREIGN KEY (Order_ID) REFERENCES $TABLE_ORDER(ID),
//            FOREIGN KEY (Status_ID) REFERENCES $TABLE_STATUS(ID)
//        )
//    """.trimIndent()

//    private val CREATE_STATUS_TABLE = """
//        CREATE TABLE $TABLE_STATUS (
//            ID INTEGER PRIMARY KEY AUTOINCREMENT,
//            StatusName TEXT NOT NULL,
//            Description TEXT
//        )
//    """.trimIndent()

    private val CREATE_ORDER_DETAIL_TABLE = """
        CREATE TABLE $TABLE_ORDER_DETAIL (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            Order_ID INTEGER NOT NULL,
            Product_ID INTEGER NOT NULL,
            Quantity INTEGER NOT NULL,
            UnitPrice REAL NOT NULL,
            FOREIGN KEY (Order_ID) REFERENCES $TABLE_ORDER(ID),
            FOREIGN KEY (Product_ID) REFERENCES $TABLE_PRODUCT(ID)
        )
    """.trimIndent()

    private val CREATE_REVIEW_ORDER_TABLE = """
    CREATE TABLE $TABLE_REVIEW_ORDER (
        ID INTEGER PRIMARY KEY AUTOINCREMENT,
        Rating INTEGER NOT NULL,
        Comment TEXT,
        ReviewDate TEXT NOT NULL,
        User_ID INTEGER NOT NULL,
        Product_ID INTEGER NOT NULL,
        FOREIGN KEY (User_ID) REFERENCES User(ID),
        FOREIGN KEY (Product_ID) REFERENCES Product(ID),
        UNIQUE(User_ID, Product_ID) ON CONFLICT REPLACE
    )
    """.trimIndent()

//    private val CREATE_REVIEW_RESTAURANT_TABLE = """
//        CREATE TABLE $TABLE_REVIEW_RESTAURANT (
//            ID INTEGER PRIMARY KEY AUTOINCREMENT,
//            Rating INTEGER NOT NULL,
//            Comment TEXT,
//            ReviewDate TEXT NOT NULL,
//            Restaurant_ID INTEGER,
//            FOREIGN KEY (Restaurant_ID) REFERENCES $TABLE_RESTAURANT(ID)
//        )
//    """.trimIndent()

    private val CREATE_CART_TABLE = """
        CREATE TABLE $TABLE_CART (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            TotalNumber INTEGER NOT NULL,
            User_ID INTEGER,
            FOREIGN KEY (User_ID) REFERENCES $TABLE_USER(ID)
        )
    """.trimIndent()

    private val CREATE_PRODUCT_CART_TABLE = """
        CREATE TABLE $TABLE_PRODUCT_CART (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            Product_ID INTEGER,
            Cart_ID INTEGER,
            Quantity INTEGER NOT NULL,
            FOREIGN KEY (Product_ID) REFERENCES $TABLE_PRODUCT(ID),
            FOREIGN KEY (Cart_ID) REFERENCES $TABLE_CART(ID)
        )
    """.trimIndent()


    fun initData() {
        val users = listOf(
            User(
                fullName = "Admin User",
                email = "admin@gmail.com",
                phoneNumber = "0123456789",
                bioInfor = "Administrator account",
                password = "123456",
                roleId = 1
            ),
            User(
                fullName = "Restaurant User",
                email = "restaurant@gmail.com",
                phoneNumber = "0987654321",
                bioInfor = "Restaurant account",
                password = "123456",
                roleId = 3
            ),
            User(
                fullName = "Customer User",
                email = "customer@gmail.com",
                phoneNumber = "0912345678",
                bioInfor = "Customer account",
                password = "123456",
                roleId = 2
            )
        )
        users.forEach {
                user -> UserDAO(context).addUser(user)
        }

        CategoryDAO(context).addCategory(name = "Món chính", description = "Delicious Italian pizza")
        CategoryDAO(context).addCategory(name = "Món ăn nhanh", description = "Juicy burgers with fresh ingredients")
        CategoryDAO(context).addCategory(name = "Đồ uống", description = "Fresh and tasty sushi rolls")

        val products = listOf(
            Product(
                name = "Bánh Canh",
                price = 50000, // Adjust price as needed
                rating = 4.5f,
                description = "A hearty noodle soup with a thick, starchy broth.",
                category = "Món chính",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fbanhcanh.webp?alt=media&token=e0639cc0-8086-46d3-af76-080ebed83d07")
            ),
            Product(
                name = "Bánh Mì",
                price = 20000, // Adjust price as needed
                rating = 4.0f,
                description = "A popular Vietnamese sandwich with various fillings.",
                category = "Món ăn nhanh",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fbanhmi.webp?alt=media&token=7e9fe5a6-2aba-44ab-8e19-a5e66eb47f77")
            ),
            Product(
                name = "Bò Bía",
                price = 30000, // Adjust price as needed
                rating = 4.2f,
                description = "A spring roll filled with vegetables and meat.",
                category = "Món ăn vặt",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fbobia.webp?alt=media&token=670ecb86-d1ea-4a50-8a7f-bbfe5d07e780")
            ),
            Product(
                name = "Bún Đậu Mắm Tôm",
                price = 40000, // Adjust price as needed
                rating = 4.8f,
                description = "A noodle dish with fried tofu, herbs, and a tangy dipping sauce.",
                category = "Món chính",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fbundaumamtom.webp?alt=media&token=ce0cb84a-eca1-4f9d-b1d8-fd40db9f8672")
            ),
            Product(
                name = "Bún Măng",
                price = 35000, // Adjust price as needed
                rating = 4.3f,
                description = "A noodle soup with bamboo shoots and pork.",
                category = "Món chính",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fbunmam.webp?alt=media&token=c5892bc7-51c9-4d01-a860-337ce9ef1379")
            ),
            Product(
                name = "Cơm Tấm",
                price = 30000, // Adjust price as needed
                rating = 4.1f,
                description = "Broken rice served with grilled pork chop and other side dishes.",
                category = "Món chính",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fcomtam.webp?alt=media&token=95a13171-c41d-4bee-b08f-a38117bb89f1")
            ),
            Product(
                name = "Hủ Tiếu Nam Vang",
                price = 45000, // Adjust price as needed
                rating = 4.7f,
                description = "A noodle soup with shrimp, pork, and vegetables.",
                category = "Món chính",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fhutieunamvang.webp?alt=media&token=29668ada-564e-4d84-8e17-5bb0af1cde12")
            ),
            Product(
                name = "Phá Lấu",
                price = 35000, // Adjust price as needed
                rating = 4.4f,
                description = "A stew made with various offal and herbs.",
                category = "Món chính",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fphalau.webp?alt=media&token=0d835716-2881-449c-b019-d72cd71b0086")
            ),
            Product(
                name = "Phở Bò",
                price = 40000, // Adjust price as needed
                rating = 4.9f,
                description = "A popular noodle soup with beef broth and rice noodles.",
                category = "Món chính",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fphobo.webp?alt=media&token=90d20361-3831-4364-9581-65825da3cfab")
            ),
            Product(
                name = "Sữa Hạt",
                price = 20000, // Adjust price as needed
                rating = 4.2f,
                description = "A healthy and delicious plant-based milk alternative.",
                category = "Đồ uống",
                images = mutableListOf("https://firebasestorage.googleapis.com/v0/b/orderfood-72ea4.appspot.com/o/imageFood%2Fsuahat.webp?alt=media&token=747e04e0-702d-4f19-9808-b6250913ae34")
            )
        )
        products.forEach {
                product ->  ProductDAO(context).addProduct(product)
        }

    }

    override fun close() {
        super.close()
        db?.close()
    }
}
