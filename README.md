# Android Get Location	

# 1.Build.Gradle(:app)
	dependencies {
			  
          implementation 'pub.devrel:easypermissions:3.0.0'             
	      implementation 'com.google.android.gms:play-services-location:18.0.0'
    }
    
    
# 2.AndroidManifest.xml

	<?xml version="1.0" encoding="utf-8"?>
	<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.lihan.getlocationtest">
    
    <uses-permission android:name="android.permission.INTERNET"></uses-permission>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"></uses-permission>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission>

      <application
      		....

# 3.Important
>fuedLocationProviderClient.lastLocation must be null	so need use 			
>requestLocationUpdates funcation
    

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
                            locationTextView.text = "${lastLocation.latitude},${lastLocation.longitude}"
                            fusedLocationProviderClient.removeLocationUpdates(this)
                        }
                    }, Looper.getMainLooper())
            }
        }
# onRequestPermissionsResult

	 override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    	) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this) // last parameter (...,this)

    }

	
