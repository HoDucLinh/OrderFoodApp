package com.ltb.orderfoodapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.ltb.orderfoodapp.R
import com.ltb.orderfoodapp.adapter.ProductAdapter
import com.ltb.orderfoodapp.adapter.ProductCartAdapter
import com.ltb.orderfoodapp.data.LocationHelper
import com.ltb.orderfoodapp.data.dao.CartDAO
import com.ltb.orderfoodapp.data.model.User
import com.ltb.orderfoodapp.data.dao.ProductDAO
import com.ltb.orderfoodapp.viewmodel.ProductCartViewModel
import com.ltb.orderfoodapp.viewmodel.ProductViewModel

class Home : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productCartViewModel: ProductCartViewModel
    private lateinit var darkTheme : Switch
    private lateinit var productDAO: ProductDAO
    private lateinit var locationHelper: LocationHelper
    private lateinit var cartDAO : CartDAO
    private var productCartNumber : Int = 0
    private var locate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        productViewModel = ProductViewModel(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val nextSearch = findViewById<TextView>(R.id.txtSearch)
        val nextCart = findViewById<ImageButton>(R.id.nextCart)
        val nextMenu = findViewById<ImageButton>(R.id.nextMenu)
        val locationUser = findViewById<TextView>(R.id.locationUser)
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        locationHelper = LocationHelper(this)

        productDAO = ProductDAO(this)

        //

        cartDAO = CartDAO(this)
        fetchUserLocation(locationUser)
        setCartCount()

//        chuyen sang trang tim kiem
        nextSearch.setOnClickListener {
            val search = Intent(this, Search::class.java)
            startActivity(search)
        }
//        chuyen sang cart
        nextCart.setOnClickListener {
            if (isLoggedIn){
                val cart = Intent(this, MyCart::class.java)
//                cart.putExtra("locationPath",locate )
                startActivity(cart)
            }
            else Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show()

        }
        nextMenu.setOnClickListener {
            if (isLoggedIn) {
                val profileIntent = Intent(this, MyMainMenu::class.java)
                startActivity(profileIntent)
            } else {
                // Nếu chưa đăng nhập, chuyển về Login
                val loginIntent = Intent(this, SignIn::class.java)
                startActivity(loginIntent)
            }
        }



    }
    override fun onStart() {
        super.onStart()
        darkTheme = findViewById(R.id.darkTheme)
        setupGridViewProduct()
        setupTheme()
        setCartCount()
        productDAO.syncProductRatings()
    }

    override fun onResume() {
        super.onResume()
        setupGridViewProduct()
        setCartCount()
    }
    private fun setupTheme() {
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)

        if (nightMode != darkTheme.isChecked) {
            darkTheme.isChecked = nightMode
        }


        AppCompatDelegate.setDefaultNightMode(
            if (nightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        darkTheme.setOnCheckedChangeListener { _, isChecked ->
            val newMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            if (AppCompatDelegate.getDefaultNightMode() != newMode) {
                AppCompatDelegate.setDefaultNightMode(newMode)
                val editor = sharedPreferences.edit()
                editor.putBoolean("night", isChecked)
                editor.apply()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        productViewModel.close()
    }

    private fun setupGridViewProduct() {
        val products = productViewModel.getProducts()
        val gridView = findViewById<GridView>(R.id.gridviewProduct)
        val adapter = ProductAdapter(this, products)
        gridView.adapter = adapter
    }

    private fun setCartCount() {

        productCartViewModel = ProductCartViewModel(this)


        productCartNumber = productCartViewModel.getCartTotal()
        val cartCount = findViewById<TextView>(R.id.cartCount)
        cartCount.text = productCartNumber.toString()


    }
    private fun fetchUserLocation(locationUser: TextView) {
        locationHelper.getCurrentLocation { location ->
            val locate = locationHelper.getAddressFromLocation(location)
            locationUser.text = locate

            val locationPath = getSharedPreferences("locationPath", MODE_PRIVATE)
            val locationEditor = locationPath.edit()
            locationEditor.putString("locationPath", locate)
            locationEditor.apply()

        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

}