package com.weather.app.data.remote

import com.weather.app.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


//Base URL=  http://api.weatherapi.com/v1
//Current Weather=/current.json or /current.xml
//Forecast	/forecast.json or /forecast.xml
//Search or Autocomplete	/search.json or /search.xml
//History	/history.json or /history.xml
//Marine	/marine.json or /marine.xml
//Future	/future.json or /future.xml
//Time Zone	/timezone.json or /timezone.xml
//Sports	/sports.json or /sports.xml
//Astronomy	/astronomy.json or /astronomy.xml
//IP Lookup	/ip.json or /ip.xml

//API_KEY= 48a0794551d34ae0b5275518242908
interface WeatherApi {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("aqi") aqi: String = "no"
    ): WeatherResponse

    @GET("current.json")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("key") apiKey: String,
        @Query("q") coordinates: String,
        @Query("aqi") aqi: String = "no"
    ): WeatherResponse
}