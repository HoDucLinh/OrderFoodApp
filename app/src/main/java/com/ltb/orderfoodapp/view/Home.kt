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
import org.w3c.dom.Text


class Home : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productCartViewModel: ProductCartViewModel
    private lateinit var darkThemeSwitch: Switch
    private lateinit var productDAO: ProductDAO
    private lateinit var locationHelper: LocationHelper
    private lateinit var cartDAO: CartDAO
    private var cartId  : Int  = -1
    private var productCartNumber: Int =0
    private lateinit var locationUser :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        enableEdgeToEdge()
        productViewModel = ProductViewModel(this)
        productDAO = ProductDAO(this)
        locationHelper = LocationHelper(this)
        cartDAO = CartDAO(this)

        // Initialize views
        val nextSearch = findViewById<TextView>(R.id.txtSearch)
        val nextCart = findViewById<ImageButton>(R.id.nextCart)
        val nextMenu = findViewById<ImageButton>(R.id.nextMenu)
        locationUser = findViewById<TextView>(R.id.locationUser)

        darkThemeSwitch = findViewById(R.id.darkTheme)
        setupTheme()

        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val user = sharedPreferences.getString("user", "")
            val userObject = Gson().fromJson(user, User::class.java)
            cartId = userObject.getCartId()
        }

        fetchUserLocation(locationUser)
        setCartCount()


        nextSearch.setOnClickListener { startActivity(Intent(this, Search::class.java)) }
        nextCart.setOnClickListener { navigateToCart() }
        nextMenu.setOnClickListener { navigateToMenu() }
    }

    override fun onStart() {
        super.onStart()
        setupGridViewProduct()
        setCartCount()
        fetchUserLocation(locationUser)
        productDAO.syncProductRatings()
    }

    override fun onResume() {
        super.onResume()
        fetchUserLocation(locationUser)
        setCartCount()
    }



    private fun setupTheme() {
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)

        darkThemeSwitch.isChecked = nightMode
        darkThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (nightMode != isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                sharedPreferences.edit().putBoolean("night", isChecked).apply()
            }
        }
    }




    private fun setupGridViewProduct() {
        val products = productViewModel.getProducts()
        val gridView = findViewById<GridView>(R.id.gridviewProduct)
        val adapter = ProductAdapter(this, products)
        gridView.adapter = adapter
    }

    private fun setCartCount() {
        productCartViewModel = ProductCartViewModel(this)
            productCartNumber = productCartViewModel.getCartTotal(cartId)
        findViewById<TextView>(R.id.cartCount)?.text = productCartNumber.toString()
    }


    private fun fetchUserLocation(locationUser: TextView) {
        if (!locationHelper.isGpsEnabled()) {
            locationHelper.promptUserToEnableGps()
            Toast.makeText(this, "Vui lòng bật GPS để lấy vị trí", Toast.LENGTH_SHORT).show()
        } else {
            locationHelper.getCurrentLocation { location ->
                val userLocation = locationHelper.getAddressFromLocation(location)
                locationUser.text = userLocation
                getSharedPreferences("locationPath", MODE_PRIVATE).edit()
                    .putString("locationPath", userLocation).apply()
            }
        }
    }

    private fun navigateToCart() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, MyCart::class.java))
        } else {
            Toast.makeText(this, "Please login to buy", Toast.LENGTH_SHORT).show()
            val signIn = Intent(this,SignIn::class.java)
            startActivity(signIn)
        }
    }

    private fun navigateToMenu() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, MyMainMenu::class.java))
        } else {
            startActivity(Intent(this, SignIn::class.java))
        }
    }


}
