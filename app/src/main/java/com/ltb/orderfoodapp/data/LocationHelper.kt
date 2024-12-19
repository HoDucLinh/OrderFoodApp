package com.ltb.orderfoodapp.data

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fun isGpsEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
    }

    fun promptUserToEnableGps() {
        if (!isGpsEnabled()) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
    }

    fun getCurrentLocation(onLocationReceived: (Location) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    onLocationReceived(it)
                } ?: run {
                    // Nếu không lấy được vị trí, yêu cầu bật GPS
                    promptUserToEnableGps()
                    Log.e("LocationHelper", "Không thể lấy vị trí hiện tại.")
                }
            }
        } else {
            // Yêu cầu quyền nếu chưa được cấp
            ActivityCompat.requestPermissions(
                context as android.app.Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1 // Mã yêu cầu quyền, có thể tùy chỉnh
            )
        }
    }

    fun getAddressFromLocation(location: Location): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var locationPath = "Không xác định"
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
}
