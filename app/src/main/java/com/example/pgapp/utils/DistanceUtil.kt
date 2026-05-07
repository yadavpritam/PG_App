package com.example.pgapp.utils

import android.location.Location

object DistanceUtil {

    fun calculateDistance(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): String {

        val results = FloatArray(1)

        Location.distanceBetween(
            startLat,
            startLng,
            endLat,
            endLng,
            results
        )

        val distanceInKm =
            results[0] / 1000

        return String.format(
            "%.1f km away",
            distanceInKm
        )
    }
}