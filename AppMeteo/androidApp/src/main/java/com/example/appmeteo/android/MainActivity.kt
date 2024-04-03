package com.example.appmeteo.android
import WeatherDataManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private val DEFAULT_CITY = "Paris"
    private var weatherData by mutableStateOf(WeatherDataModel())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var cityName by remember { mutableStateOf(readCityNameFromFile()) }

            Column {
                TextField(
                    value = cityName,
                    onValueChange = {
                        cityName = it
                    },
                    label = { Text("Obtenir les données météos") },
                    modifier = Modifier.padding(16.dp)
                )

                Button(
                    onClick = {
                        if (cityName.isNotBlank()) {
                            writeCityNameToFile(cityName)
                            lifecycleScope.launch {
                                val data = withContext(Dispatchers.IO) {
                                    val weatherDataManager = WeatherDataManager()
                                    weatherDataManager.fetchWeather(cityName)
                                }
                                weatherData = data
                            }
                        }

                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Sauvegarder le nom de la ville")
                }
                WeatherDataDisplay(weatherData)
            }
        }
    }

    private fun writeCityNameToFile(cityName: String) {
        try {
            openFileOutput("city.txt", Context.MODE_PRIVATE).use {
                it.write(cityName.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readCityNameFromFile(): String {
        var cityName = ""
        try {
            openFileInput("city.txt").use {
                cityName = it.bufferedReader().readText()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (cityName.isNotBlank()) cityName else DEFAULT_CITY
    }

    @Composable
    fun WeatherDataDisplay(data: WeatherDataModel) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Température actuelle: ${data.currentTemperature}°C")
            Text(text = "Température minimale sur 24h: ${data.minTemperature}°C")
            Text(text = "Température maximale sur 24h: ${data.maxTemperature}°C")
            Text(text = "Type de temps: ${data.weatherCondition}")
            Text(text = "Vitesse du vent: ${data.windSpeed} m/s")
            Text(text = "Indice UV: ${data.uvIndex}")
        }
    }
}