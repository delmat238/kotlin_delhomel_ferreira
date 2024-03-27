package com.example.appmeteo.android
import WeatherDataManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf



class MainActivity : ComponentActivity() {
    private val weatherDataManager = WeatherDataManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var cityName by remember { mutableStateOf("") }
            val weatherData = remember { mutableStateOf(WeatherDataModel(0.0, 0.0, 0.0, "", 0.0, 0.0)) }

            Column {

                TextField(
                    value = cityName,
                    onValueChange = { cityName = it },
                    label = { Text("Nom de la ville") },
                    modifier = Modifier.padding(16.dp)
                )

                Button(
                    onClick = {
                        if (cityName.isNotBlank()) {
                            lifecycleScope.launch {
                                val data = withContext(Dispatchers.IO) {
                                    weatherDataManager.fetchWeather(cityName)
                                }
                                weatherData.value = data
                            }
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Obtenir les données météorologiques")
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

                MyApplicationTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        WeatherDataDisplay(weatherData.value)
                    }
                }
            }
        }
    }


}


