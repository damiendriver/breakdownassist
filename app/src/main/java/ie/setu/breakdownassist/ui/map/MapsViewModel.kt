package ie.setu.breakdownassist.ui.map

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import timber.log.Timber

@SuppressLint("MissingPermission")
class MapsViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var map: GoogleMap
    var currentLocation = MutableLiveData<Location>()
    var locationClient: FusedLocationProviderClient
    val locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(10000)  // Set the update interval in milliseconds
        .setFastestInterval(5000)
        .setMaxWaitTime(15000)

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            currentLocation.value = locationResult.locations.last()
        }
    }

    init {
        locationClient = LocationServices.getFusedLocationProviderClient(application)
        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun updateCurrentLocation() {
        locationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLocation.value = location
                    Timber.i("MAP VM LOC SUCCESS: %s", currentLocation.value)
                } else {
                    Timber.e("Failed to get last location.")
                }
            }
    }
}
