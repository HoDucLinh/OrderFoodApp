package com.ltb.orderfoodapp.data

import android.Manifest
import android.R
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import java.util.Locale



class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationPath : String = ""

    fun getCurrentLocation(onLocationReceived: (Location) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    onLocationReceived(it)
                } ?: run {
                    Log.e("LocationHelper", "Không thể lấy vị trí hiện tại.")
                }
            }
        } else {
            Log.e("LocationHelper", "Quyền truy cập vị trí không được cấp.")
        }
    }
    fun getAddressFromLocation(location: Location): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        locationPath = "Không xác định"
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val components = mutableListOf<String>()

                // Lấy các thành phần của địa chỉ
                val houseAndStreet = address.getAddressLine(0)

                // Kiểm tra và thêm các thành phần vào list
                if (!houseAndStreet.isNullOrEmpty() && !houseAndStreet.contains("Unnamed Road")) {
                    components.add(houseAndStreet.trim())
                }

                // Lấy xã/phường (subLocality) nếu có
                address.subLocality?.let {
                    components.add(it.trim())
                }

                // Lấy quận/huyện (locality) nếu có
                address.locality?.let {
                    components.add(it.trim())
                }
                // Nối tất cả các thành phần lại với nhau
                locationPath = components.joinToString(", ")

                // Nếu không có thông tin gì, gán là không xác định
                if (locationPath.isEmpty()) {
                    locationPath = "Không xác định"
                }
            } else {
                Log.e("LocationHelper", "Không tìm thấy địa chỉ cho vị trí này.")
            }
        } catch (e: Exception) {
            Log.e("LocationHelper", "Lỗi khi lấy địa chỉ: ${e.message}")
        }
        return locationPath
    }



    fun fetchAddressSuggestions(query: String, autoCompleteTextView: AutoCompleteTextView) {
        // Nếu query rỗng, không cần tìm kiếm
        if (query.isEmpty()) {
            autoCompleteTextView.setAdapter(null)
            return
        }

        val geocoder = Geocoder(context, Locale.getDefault())
        val handler = Handler(Looper.getMainLooper())

        // Khai báo biến để lưu Runnable hiện tại
        var searchRunnable: Runnable? = null

        // Hủy bỏ Runnable cũ nếu có
        searchRunnable?.let { handler.removeCallbacks(it) }

        // Tạo Runnable mới để tìm kiếm sau 5 giây không có thay đổi
        searchRunnable = Runnable {
            try {
                // Lấy danh sách địa chỉ từ Geocoder
                val addresses = geocoder.getFromLocationName(query, 5)

                if (!addresses.isNullOrEmpty()) {
                    val addressList = mutableListOf<String>()
                    for (address in addresses) {
                        addressList.add(address.getAddressLine(0))
                    }

                    val adapter = ArrayAdapter(context, R.layout.simple_dropdown_item_1line, addressList)
                    autoCompleteTextView.setAdapter(adapter)
                } else {
                    autoCompleteTextView.setAdapter(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Lỗi khi lấy gợi ý địa chỉ", Toast.LENGTH_SHORT).show()
            }
        }

        // Đặt Runnable này để thực hiện sau 5 giây không nhập
        handler.postDelayed(searchRunnable, 5000) // 5000ms = 5 giây
    }


}

