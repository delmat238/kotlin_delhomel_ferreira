package com.example.appmeteo.android

import WeatherDataManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmeteo.android.view.Home
import com.example.appmeteo.android.view.Favoris
import com.example.appmeteo.android.view.Prevision
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val DEFAULT_CITY = "Paris"
    private var weatherData by mutableStateOf(WeatherDataModel())
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navControler = rememberNavController()
                val routeHome = "Home"
                val routeFavoris = "Favoris"
                var selectedTab by remember { mutableStateOf("Home") }
                Scaffold(
                    content = { innerPadding ->
                        Box() {
                            val backgroundImage = painterResource(id = R.drawable.fond_ciel)
                            Image(
                                painter = backgroundImage,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop
                            )
                            NavHost(
                                navController = navControler, startDestination = "Home",
                            ) {
                                composable(
                                    route = "Home",
                                ) {
                                    Home(applicationContext, innerPadding)
                                }
                                composable(
                                    route = "Favoris",
                                ) {
                                    Favoris()
                                }
                                composable(
                                    route = "Prevision",
                                ) {
                                    Prevision()
                                }
                            }
                        }
                    },

                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(selected = selectionneIcon(selectedTab, "Home"),
                                onClick = {
                                    navControler.navigate("Home")
                                    selectedTab = "Home"
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Home, contentDescription = null
                                    )
                                },
                                label = { Text(text = "Home") })

                            NavigationBarItem(selected = selectionneIcon(selectedTab, "Favoris"),
                                onClick = {
                                    navControler.navigate("Favoris")
                                    selectedTab = "Favoris"
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Star, contentDescription = null
                                    )
                                },
                                label = {
                                    Text(text = "Favoris")
                                })

                            NavigationBarItem(selected = selectionneIcon(selectedTab, "Prevision"),
                                onClick = {
                                    navControler.navigate("Prevision")
                                    selectedTab = "Prevision"
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text(text = "Prevision")
                                })
                        }
                    }
                )
            }
        }
    }

    private fun selectionneIcon(selectedTab: String, r: String): Boolean {
        return selectedTab == r
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
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "Température actuelle: ${data.currentTemperature}°C")
                Text(text = "Température minimale sur 24h: ${data.minTemperature}°C")
                Text(text = "Température maximale sur 24h: ${data.maxTemperature}°C")
                Text(text = "Type de temps: ${data.weatherCondition}")
                Text(text = "Vitesse du vent: ${data.windSpeed} m/s")
                Text(text = "Indice UV: ${data.uvIndex}")
            }
        }
    }

    private fun fetchWeatherData(cityName: String) {
        coroutineScope.launch {
            val data = withContext(Dispatchers.IO) {
                val weatherDataManager = WeatherDataManager()
                weatherDataManager.fetchWeather(cityName)
            }
            weatherData = data
        }
    }
}
