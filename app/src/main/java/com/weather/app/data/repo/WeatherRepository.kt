package com.weather.app.data.repo

import com.weather.app.data.model.WeatherResponse
import com.weather.app.data.remote.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: WeatherApi
) {

    suspend fun getWeather(location: String, apiKey: String = "48a0794551d34ae0b5275518242908"): WeatherResponse {
        return apiService.getCurrentWeather(apiKey, location)
    }

    suspend fun getWeatherByCoordinates(latitude: Double, longitude: Double, apiKey: String = "48a0794551d34ae0b5275518242908"): WeatherResponse {
        val coordinates = "$latitude,$longitude"
        return apiService.getCurrentWeatherByCoordinates(apiKey, coordinates)
    }
}