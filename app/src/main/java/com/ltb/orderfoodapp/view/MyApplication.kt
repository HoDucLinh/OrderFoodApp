package com.ltb.orderfoodapp.view

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.ltb.orderfoodapp.data.api.AuthManager
import com.ltb.orderfoodapp.data.dao.CategoryDAO
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.data.dao.UserDAO
import com.ltb.orderfoodapp.data.model.Product
import com.ltb.orderfoodapp.data.model.User

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupNightMode()
        initData(this)
        // Khởi tạo Firebase
        FirebaseApp.initializeApp(this)

        // Cấu hình Firebase App Check
        val providerFactory = PlayIntegrityAppCheckProviderFactory.getInstance()
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(providerFactory)
    }
    private fun setupNightMode() {
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)
        AppCompatDelegate.setDefaultNightMode(
            if (nightMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }



    fun initData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isDataInitialized = sharedPreferences.getBoolean("isDataInitialized", false)

        if (isDataInitialized) {
            // Nếu dữ liệu đã được khởi tạo, thoát khỏi hàm
            return
        }
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
            AuthManager(context).createAccount(user.getEmail(),user.getPassword())

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
        sharedPreferences.edit().putBoolean("isDataInitialized", true).apply()

    }
}
