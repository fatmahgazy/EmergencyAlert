package org.codeforegypt.quickassestant.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationProvider(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun observeLocationUpdate(): Flow<Location?> {
        return callbackFlow {

            val client = LocationServices.getFusedLocationProviderClient(context)

            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(true)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationReult: LocationResult) {
                    trySend(locationReult.lastLocation)
                }
            }

            client.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            ).addOnFailureListener {
                close(it)
            }

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

}