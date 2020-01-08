package app.plantdiary.myplantdiaryktprep

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import app.plantdiary.myplantdiaryktprep.dto.LocationDetails
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationLiveData(context: Context) : LiveData<LocationDetails>() {
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? -> location?.also {
                setLocationData(it)
            }
        }
        startLocationUpdates()
    }

    private fun setLocationData(location: Location) {
        value = LocationDetails(
            longitude = location.longitude.toString(),
            latitude = location.latitude.toString()
        )
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 60000
            fastestInterval = 12500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            // pre-emptive null check?
            locationResult ?: return
            // ok... we're not null here.
            for(location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

}