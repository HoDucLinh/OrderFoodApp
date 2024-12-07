package com.ltb.orderfoodapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
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
import com.ltb.orderfoodapp.viewmodel.ProductCartViewModel
import com.ltb.orderfoodapp.viewmodel.ProductViewModel
import org.w3c.dom.Text
import java.util.Locale
import kotlin.math.log

class Home : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productCartViewModel: ProductCartViewModel
    private lateinit var darkTheme : Switch
    private lateinit var productCartAdapter: ProductCartAdapter
    private lateinit var locationHelper: LocationHelper
    private lateinit var cartDAO : CartDAO
    private var productCartNumber : Int = 0
    private var userId : Int = -1
    private var cartId : Int = -1
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
        if(isLoggedIn){

        }

        locationHelper = LocationHelper(this)


        cartDAO = CartDAO(this)
        setupLocation(locationUser)
        setCartCount()

//        chuyen sang trang tim kiem
        nextSearch.setOnClickListener {
            val nextSearch = Intent(this, Search::class.java)
            startActivity(nextSearch)
        }
//        chuyen sang cart
        nextCart.setOnClickListener {
            if (isLoggedIn){
                val nextCart = Intent(this, MyCart::class.java)
                startActivity(nextCart)
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
    }

    override fun onResume() {
        super.onResume()
        setupGridViewProduct()
        setCartCount()
    }
    private fun setupTheme() {
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("night", false)

        // Kiểm tra trạng thái theme hiện tại và thay đổi nếu cần
        if (nightMode != darkTheme.isChecked) {
            darkTheme.isChecked = nightMode
        }

        // Thiết lập chế độ tối/sáng
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

        productCartAdapter = ProductCartAdapter(this, productCartViewModel.getProduct())
        productCartNumber = productCartAdapter.itemCount
        val cartCount = findViewById<TextView>(R.id.cartCount)
        cartCount.text = productCartNumber.toString()


    }
    private fun setupLocation(locationUser: TextView) {
        // Kiểm tra quyền vị trí
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            fetchUserLocation(locationUser)
        }
    }

    private fun fetchUserLocation(locationUser: TextView) {
        locationHelper.getCurrentLocation { location ->
            if (location != null) {
                val locate = locationHelper.getAddressFromLocation(location)
                locationUser.setText(locate)
                Toast.makeText(this, "${locate}", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("LocationHelper", "Không thể lấy vị trí hiện tại.")
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

}