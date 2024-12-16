package com.ltb.orderfoodapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var db: SQLiteDatabase? = null

    companion object {
        private const val DATABASE_NAME = "oderfoodapp.db"
        private const val DATABASE_VERSION = 2

        // Tên bảng và các cột
        private const val TABLE_CATEGORY = "Category"
        private const val TABLE_PRODUCT = "Product"
        private const val TABLE_IMAGE = "Image"
        private const val TABLE_ADDRESS_USER = "AddressOfUser"
        private const val TABLE_RESTAURANT = "Restaurant"
        private const val TABLE_USER = "User"
        private const val TABLE_ROLE = "Role"
        private const val TABLE_ORDER = "\"Order\""

        //        private const val TABLE_ORDER_STATUS = "OrderStatus"
//        private const val TABLE_STATUS = "Status"
        private const val TABLE_ORDER_DETAIL = "OrderDetail"
        private const val TABLE_REVIEW_ORDER = "ReviewOrder"
        private const val TABLE_REVIEW_RESTAURANT = "ReviewRestaurant"
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
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_IMAGE")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADDRESS_USER")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESTAURANT")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROLE")
////        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNT")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER")
////        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER_STATUS")
////        db.execSQL("DROP TABLE IF EXISTS $TABLE_STATUS")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER_DETAIL")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEW_ORDER")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEW_RESTAURANT")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT_CART")
//        onCreate(db)
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
        FOREIGN KEY (User_ID) REFERENCES $TABLE_USER(ID),
        FOREIGN KEY (Product_ID) REFERENCES $TABLE_PRODUCT(ID),
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


    override fun close() {
        super.close()
        db?.close()
    }
}
