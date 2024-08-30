package com.weather.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.weather.app.ui.screens.WeatherScreen
import com.weather.app.ui.theme.WeatherApplicationTheme
import com.weather.app.ui.vm.WeatherIntent
import com.weather.app.ui.vm.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private val REQUEST_CODE_LOCATION_PERMISSION = 1
    private val REQUEST_CHECK_SETTINGS = 1001

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkLocationSettings()
        } else {
            Log.e("MainActivity", "Location permission denied")
        }
    }
//    private val locationPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                viewModel.handleIntent(WeatherIntent.FetchWeatherByLocation)
//            } else {
//                // Handle permission denied scenario
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherScreen(viewModel)
            //WeatherView(viewModel, textColor = Color.White)
            observeViewModel()
        }
        checkLocationPermissionAndFetchWeather()
//        // Request location permission on startup (if necessary)
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
//            requestLocationPermission()
//        }

//        // Observe location settings event
//        lifecycleScope.launchWhenStarted {
//            viewModel.locationSettingsEvent.collect { exception ->
//                exception?.let {
//                    resolveLocationSettings(it)
//                }
//            }
//        }

    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                state.error?.let { error ->
                    if (error == "Location settings are inadequate and need to be resolved") {
                        promptUserToEnableLocationSettings()
                    } else {
                        // Show error message
                        Log.e("MainActivity", error)
                    }
                }
            }
        }
    }

    private fun checkLocationPermissionAndFetchWeather() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
                checkLocationSettings()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Show a rational dialog
                Log.e("MainActivity", "Location permission is needed to show weather based on location")
            }
            else -> {
                // Request permission
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

//    private fun resolveLocationSettings(exception: ResolvableApiException) {
//        try {
//            exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
//        } catch (sendEx: IntentSender.SendIntentException) {
//            Log.e("MainActivity", "Error starting resolution for location settings", sendEx)
//        }
//    }

//    private fun requestLocationPermission() {
//        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//    }

//    private fun checkLocationSettings() {
//        val locationRequest = LocationRequest.create().apply {
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//
//        val client: SettingsClient = LocationServices.getSettingsClient(this)
//        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
//
//        task.addOnSuccessListener {
//            // Location settings are satisfied.
//            viewModel.fetchWeatherByLocation()
//        }
//
//        task.addOnFailureListener { exception ->
//            if (exception is ResolvableApiException) {
//                try {
//                    exception.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    Log.e("MainActivity", "Error checking location settings", sendEx)
//                }
//            } else {
//                Log.e("MainActivity", "Location settings are inadequate and cannot be fixed here.")
//                viewModel.setErrorState("Location settings need to be enabled")
//            }
//        }
//    }

    private fun checkLocationSettings() {
        viewModel.handleIntent(WeatherIntent.FetchWeatherByLocation)
    }

    private fun promptUserToEnableLocationSettings() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e("MainActivity", "Error checking location settings", sendEx)
                }
            }
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                // Permission granted
//                //viewModel.handleIntent(WeatherIntent.FetchWeatherByLocation)
//                viewModel.checkLocationSettings()
//            } else {
//                // Permission denied
//                Log.e("WeatherActivity", "Location permission denied")
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.handleIntent(WeatherIntent.FetchWeatherByLocation)
            } else {
                viewModel.setErrorState("Location settings need to be enabled")
                Log.e("MainActivity", "Location settings change request denied")
            }
        }
    }

}

