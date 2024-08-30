package com.weather.app.ui.vm

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.weather.app.data.model.WeatherResponse
import com.weather.app.data.repo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WeatherIntent {
    data class FetchWeather(val location: String) : WeatherIntent()
    object FetchWeatherByLocation : WeatherIntent()
}

data class WeatherViewState(
    val weather: WeatherResponse? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val settingsClient: SettingsClient // Inject the SettingsClient
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherViewState())
    val state: StateFlow<WeatherViewState> = _state


    fun handleIntent(intent: WeatherIntent) {
        when (intent) {
            is WeatherIntent.FetchWeather -> fetchWeather(intent.location)
            is WeatherIntent.FetchWeatherByLocation -> checkLocationSettingsAndFetchWeather()  //fetchWeatherByLocation()
        }
    }

    private fun fetchWeather(location: String) {
        viewModelScope.launch {
            _state.value = WeatherViewState(isLoading = true)
            try {
                val weather = repository.getWeather(location)
                _state.value = WeatherViewState(weather = weather)
            } catch (e: Exception) {
                _state.value = WeatherViewState(error = e.message)
                Log.e("WeatherViewModel", "Error fetching weather", e) // Adding log for better debugging
            }
        }
    }

//    @SuppressLint("MissingPermission")
//    fun fetchWeatherByLocation() {
//        viewModelScope.launch {
//            _state.value = WeatherViewState(isLoading = true)
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                location?.let {
//                    fetchWeatherByCoordinates(it.latitude, it.longitude)
//                } ?: run {
//                    _state.value = WeatherViewState(error = "Unable to retrieve location")
//                }
//            }.addOnFailureListener {
//                _state.value = WeatherViewState(error = it.message)
//                Log.e("WeatherViewModel", "Error retrieving location", it) // Adding log for better debugging
//            }
//        }
//    }

    @SuppressLint("MissingPermission")
    private fun checkLocationSettingsAndFetchWeather() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val task: Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Location settings are satisfied, initiate location request
            fetchWeatherByLocation()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                _state.value = WeatherViewState(error = "Location settings are inadequate and need to be resolved")
                // Notify the UI to show the dialog to the user
            } else {
                _state.value = WeatherViewState(error = "Location settings are inadequate and cannot be fixed here.")
                Log.e("WeatherViewModel", "Location settings are inadequate and cannot be fixed here.")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchWeatherByLocation() {
        viewModelScope.launch {
            _state.value = WeatherViewState(isLoading = true)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    fetchWeatherByCoordinates(location.latitude, location.longitude)
                } else {
                    requestNewLocationData()
                }
            }.addOnFailureListener {
                _state.value = WeatherViewState(error = it.message)
                Log.e("WeatherViewModel", "Error retrieving location", it)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
            numUpdates = 1
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            if (location != null) {
                fetchWeatherByCoordinates(location.latitude, location.longitude)
            } else {
                _state.value = WeatherViewState(error = "Unable to retrieve location even after requesting updates")
                Log.e("WeatherViewModel", "Unable to retrieve location even after requesting updates")
            }
        }
    }


    private fun fetchWeatherByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val weather = repository.getWeatherByCoordinates(latitude, longitude)
                _state.value = WeatherViewState(weather = weather)
            } catch (e: Exception) {
                _state.value = WeatherViewState(error = e.message)
                Log.e("WeatherViewModel", "Error fetching weather by coordinates", e) // Adding log for better debugging
            }
        }
    }

    // Method to set error state directly
    fun setErrorState(message: String) {
        _state.value = WeatherViewState(error = message)
    }


}