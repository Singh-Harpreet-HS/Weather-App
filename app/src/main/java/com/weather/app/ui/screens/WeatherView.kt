//package com.weather.app.ui.screens
//
//import android.Manifest
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.media3.common.util.UnstableApi
//import com.weather.app.R
//import com.weather.app.ui.vm.WeatherIntent
//import com.weather.app.ui.vm.WeatherViewModel
//@UnstableApi
//@Composable
//fun WeatherView(weatherViewModel: WeatherViewModel = hiltViewModel(), textColor: Color) {
//
//    val state by weatherViewModel.state.collectAsState()
//    val context = LocalContext.current
//
//    var location by remember { mutableStateOf("") }
//
//    // Determine which video to play based on the weather condition
//    val videoResourceId = remember(state.weather?.current?.condition?.text) {
//        when {
//            state.weather?.current?.condition?.text?.contains("cloudy", ignoreCase = true) == true -> R.raw.cloudy_weather
//            state.weather?.current?.condition?.text?.contains("sunny", ignoreCase = true) == true -> R.raw.sunny_weather
//            state.weather?.current?.condition?.text?.contains("rainy", ignoreCase = true) == true -> R.raw.raining_video
//            else -> R.drawable.ic_launcher_foreground // Fallback video resource if condition is neither cloudy nor sunny
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        VideoBackground(
//            resourceId = R.raw.cloudy_weather, // Replace with your video resource ID
//            modifier = Modifier.matchParentSize()
//        )
//
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//
//            OutlinedTextField(
//                value = location,
//                onValueChange = { location = it },
//                label = { Text("Enter Location") },
//                modifier = Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = { weatherViewModel.handleIntent(WeatherIntent.FetchWeather(location))  }) {
//                Text("Get Weather")
//            }
//
//            state.weather?.let { weather ->
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(text = "Location: ${weather.location.name}", color = textColor, fontSize = 24.sp)
//                    Text(text = "Temperature: ${weather.current.temp_c}°C", color = textColor, fontSize = 24.sp)
//                    Text(text = "Condition: ${weather.current.condition.text}", color = textColor, fontSize = 24.sp)
//                    Text(text = "Humidity: ${weather.current.humidity} ", color = textColor, fontSize = 14.sp)
//                    Text(text = "UV: ${weather.current.uv}", color = textColor, fontSize = 14.sp)
//                    Text(text = "FeelsLike: ${weather.current.feelslike_c} °C ", color = textColor, fontSize = 14.sp)
//                    Text(text = "Wind Speed: ${weather.current.wind_kph} KPH%", color = textColor, fontSize = 14.sp)
//                    Text(text = "Last Updated: ${weather.current.last_updated} %", color = textColor, fontSize = 14.sp)
//                }
//            }
//
//
//            if (state.isLoading) {
//                CircularProgressIndicator()
//            }
//
//            state.error?.let { error ->
//                Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = {
//                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
//                    //weatherViewModel.handleIntent(WeatherIntent.FetchWeatherByLocation)
//                } else {
//                    val activity = context as androidx.activity.ComponentActivity
//                    activity.requestPermissions(
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                        1
//                    )
//                }
//            }) {
//                Text("Get Weather by Location")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//


