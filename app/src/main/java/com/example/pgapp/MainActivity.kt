package com.example.pgapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pgapp.navigation.AppNavGraph
import com.example.pgapp.ui.theme.PGAppTheme
import com.example.pgapp.utils.LocationPermissionHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissionLauncher =

        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =

                permissions[
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ] == true

            if (granted) {

                LocationPermissionHelper.checkGps(this)

            } else {

                Toast.makeText(
                    this,
                    "Please allow location permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ASK LOCATION PERMISSION
        permissionLauncher.launch(

            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        LocationPermissionHelper.checkGps(this)

        enableEdgeToEdge()

        setContent {

            PGAppTheme {

                AppNavGraph()
            }
        }
    }
}