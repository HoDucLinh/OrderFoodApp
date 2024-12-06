package com.ltb.orderfoodapp.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.util.Log
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
    private lateinit var locationPath : String

    fun getCurrentLocation(onLocationReceived: (Location) -> Unit) {
        // Kiểm tra quyền truy cập vị trí
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
    fun getAddressFromLocation(location: Location) : String {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressLine = address.getAddressLine(0)
                val parts = addressLine.split("/")
                locationPath = "${parts[1]} ${parts[0]} "
            } else {
                Log.e("LocationHelper", "Không tìm thấy địa chỉ cho vị trí này.")
            }
        } catch (e: Exception) {
            Log.e("LocationHelper", "Lỗi khi lấy địa chỉ: ${e.message}")
        }
        return locationPath
    }


}