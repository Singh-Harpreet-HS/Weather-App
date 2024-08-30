package com.weather.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weather.app.ui.vm.WeatherIntent
import com.weather.app.ui.vm.WeatherViewModel
import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.weather.app.R


@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel = hiltViewModel()) {
    val state by weatherViewModel.state.collectAsState()
    val context = LocalContext.current

    var location by remember { mutableStateOf("") }

    val picResourceId = remember(state.weather?.current?.condition?.text) {
        when {
            state.weather?.current?.condition?.text?.contains("cloudy", ignoreCase = true) == true -> R.drawable.cloudy
            state.weather?.current?.condition?.text?.contains("clouds", ignoreCase = true) == true -> R.drawable.cloudy
            state.weather?.current?.condition?.text?.contains("sunny", ignoreCase = true) == true -> R.drawable.img
            state.weather?.current?.condition?.text?.contains("rainy", ignoreCase = true) == true -> R.drawable.img_1
            state.weather?.current?.condition?.text?.contains("rain", ignoreCase = true) == true -> R.drawable.img_1

            else -> R.drawable.cloudy // Fallback video resource if condition is neither cloudy nor sunny
        }
    }

    val weatherEmoji = getWeatherEmoji(state.weather?.current?.condition?.text)

    Box(modifier = Modifier.fillMaxSize()) {

        Image(painter = painterResource(id = picResourceId),
            contentDescription = "weather pics",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .alpha(0.5f)
                .fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Enter Location") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {

                Button(onClick = { weatherViewModel.handleIntent(WeatherIntent.FetchWeather(location)) }) {
                    Text("Get Weather")
                }

                IconButton(onClick = {
                    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    weatherViewModel.handleIntent(WeatherIntent.FetchWeatherByLocation)
                } else {
                    val activity = context as androidx.activity.ComponentActivity
                    activity.requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                } }) {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "Refresh")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            state.weather?.let { weather ->
                Row(modifier = Modifier) {
                            Column(horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .background(
                                        Color.Black.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "\uD83D\uDCCD: ${weather.location.name}",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "\uD83C\uDF21\uFE0F: ${weather.current.temp_c}°C/${weather.current.temp_f}°F",
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Feels Like: ${weather.current.feelslike_c}°C/${weather.current.feelslike_f}°F",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "${weather.current.condition.text}",
                                    color = Color.White,
                                    fontSize = 24.sp
                                )

                                Text(
                                    text = "\uD83D\uDCA8: ${weather.current.wind_kph} KPH",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                )

                                Text(
                                    text = "\uD83D\uDCA6 Humidity: ${weather.current.humidity}",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                )

                                Text(
                                    text = "Wind Dir: ${weather.current.wind_dir}",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                )

                                Text(
                                    text = "Precipitation: ${weather.current.precip_mm} mm",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                )

                                Text(
                                    text = "UV: ${weather.current.uv} ",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                )

                                Text(
                                    text = "Last Updated on: ${weather.current.last_updated} ",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                )
                        }
                    Spacer(modifier = Modifier.width(16.dp))

                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                Color.Black.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)) {
                        Text(text = "$weatherEmoji", fontSize = 40.sp) // Display the weather emoji
                    }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            }

            state.error?.let { error ->
                Row(horizontalArrangement = Arrangement.Center) {

                    Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Button(onClick = {
                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    weatherViewModel.handleIntent(WeatherIntent.FetchWeatherByLocation)
                } else {
                    val activity = context as androidx.activity.ComponentActivity
                    activity.requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                }
            }) {
                Text("Get Weather by Location")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}

val weatherEmoji = mapOf(
    "cloudy" to "☁️",
    "clouds" to "☁️",
    "sunny" to "☀️",
    "rain" to "🌧️",
    "rainy" to "🌧️",
    "thunderstorm" to "⛈️",
    "snow" to "❄️",
    "fog" to "🌫️",
    "clear" to "🌕",
    "partly cloudy" to "🌤️",
    "mist" to "\uD83C\uDF2B\uFE0F",
//    "Cloudy" to "☁️",
//    "Clouds" to "☁️",
//    "Sunny" to "☀️",
//    "Rain" to "🌧️",
//    "Rainy" to "🌧️",
//    "Thunderstorm" to "⛈️",
//    "Snow" to "❄️",
//    "Fog" to "🌫️",
//    "Clear" to "🌕",
//    "Partly cloudy" to "🌤️",
//    "Mist" to "\uD83C\uDF2B\uFE0F",
)

// Function to get the appropriate emoji based on the condition
fun getWeatherEmoji(condition: String?): String {
    if (condition == null) return "❓" // Default emoji for unknown condition
    for ((key, value) in weatherEmoji) {
        if (condition.contains(key, ignoreCase = true)) {
            return value
        }
    }
    return "❓" // Default emoji if no match found
}


@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreen(){
    WeatherScreen()
}
