package com.example.pgapp.utils

import android.content.IntentSender
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

object LocationPermissionHelper {

    fun checkGps(
        activity: ComponentActivity
    ) {

        val locationRequest =

            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                1000
            ).build()

        val builder =

            LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        val client =
            LocationServices.getSettingsClient(activity)

        val task =
            client.checkLocationSettings(
                builder.build()
            )

        // GPS ALREADY ON
        task.addOnSuccessListener {

            Toast.makeText(
                activity,
                "Location enabled successfully 📍",
                Toast.LENGTH_SHORT
            ).show()
        }

        // GPS OFF
        task.addOnFailureListener { exception ->

            Toast.makeText(
                activity,
                "Please turn on your location to continue",
                Toast.LENGTH_LONG
            ).show()

            if (exception is ResolvableApiException) {

                try {

                    exception.startResolutionForResult(
                        activity,
                        1001
                    )

                } catch (_: IntentSender.SendIntentException) {
                }
            }
        }
    }
}