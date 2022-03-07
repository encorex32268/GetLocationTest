package com.lihan.getlocationtest

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

class MainActivity : AppCompatActivity() , EasyPermissions.PermissionCallbacks {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationTextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationTextView = findViewById(R.id.locationTextView)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (hasLocationPermission()){
            showLocation()
        }else{
            requestLocationPermissions()
        }

    }


    @SuppressLint("MissingPermission")
    private fun showLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            it?:run{
                val request = LocationRequest.create().apply {
                    interval = 5000
                    fastestInterval = 3000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(request,
                    object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            super.onLocationResult(result)
                            val lastLocation = result.lastLocation
                            locationTextView.text = "${lastLocation.latitude}, ${lastLocation.longitude}"
                            fusedLocationProviderClient.removeLocationUpdates(this)
                        }
                    }, Looper.getMainLooper())
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    private fun requestLocationPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "This app cannot work without Location Permission",
            101,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        showLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestLocationPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }
}