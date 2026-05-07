    package com.example.pgapp.utils


    import android.annotation.SuppressLint
    import android.location.Location
    import com.google.android.gms.location.LocationServices
    import android.content.Context
    import kotlinx.coroutines.suspendCancellableCoroutine
    import javax.inject.Inject
    import dagger.hilt.android.qualifiers.ApplicationContext


    class LocationHelper @Inject constructor(
        @ApplicationContext val context: Context
    ){
        private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        @SuppressLint("MissingPermission")
        suspend fun getCurrentLocation(): Location? {

            return try {

                suspendCancellableCoroutine { cont ->

                    fusedClient.lastLocation
                        .addOnSuccessListener {
                            cont.resume(it) {}
                        }
                        .addOnFailureListener {
                            cont.resume(null) {}
                        }
                }

            } catch (e: Exception) {
                null
            }
        }
    }