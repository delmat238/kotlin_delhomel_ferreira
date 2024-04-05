package com.example.appmeteo.android.view

import WeatherDataManager
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.appmeteo.android.WeatherDataModel
import kotlinx.coroutines.launch

@Composable
fun Home(applicationContext: Context, innerPadding: PaddingValues) {
    SearchBar(applicationContext, innerPadding)
}


@Composable
fun SearchBar(applicationContext: Context, innerPadding: PaddingValues) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    val (ville, setVille) = remember { mutableStateOf("") }
    val (weatherData, setWeatherData) = remember { mutableStateOf(WeatherDataModel()) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding) // Utilise toute la largeur disponible
    ) {
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            singleLine = true,
            modifier = Modifier.padding(10.dp)
        )

        Row {
            Button(onClick = {
                // Appel de fetchWeather lorsque l'utilisateur valide la ville depuis la barre de recherche
                val weatherDataManager = WeatherDataManager()
                coroutineScope.launch {
                    val weatherData = weatherDataManager.fetchWeather(text)
                    // Mettre à jour l'état de l'application avec les données météorologiques
                    setVille(text)
                    setWeatherData(weatherData)
                }
            }) {
                Text("Valider")
            }

            // Bouton pour utiliser la géolocalisation
            Spacer(modifier = Modifier.padding(2.dp))
            Button(onClick = {
                btnLocalisation(context)
                setVille("Position actuelle")
            }) {
                Text("Utiliser géolocalisation")
            }
        }
    }

    // Afficher les données météorologiques
    AfficherMeteo(ville = ville, weatherData = weatherData)
}

@SuppressLint("MissingPermission")
fun btnLocalisation(context: Context) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    if (locationManager != null) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                println("Latitude: $latitude, Longitude: $longitude")
            }
        } else {
            // Si les autorisations de localisation ne sont pas accordées, demandez-les à l'utilisateur
            // Notez que cette demande d'autorisations doit être effectuée avant d'appeler getLastKnownLocation()
            // pour éviter des problèmes de sécurité
            // Vous pouvez appeler la fonction requestLocationPermission() ici ou gérer les autorisations d'une autre manière
        }
    }
}

@Composable
fun AfficherMeteo(ville: String, weatherData: WeatherDataModel) {
    val shape: Shape = androidx.compose.foundation.shape.RoundedCornerShape(5.dp)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.White.copy(0.3f), shape)
                .width(330.dp)
                .height(550.dp)
        ) {
            Column {
                Text(
                    text = ville, style = TextStyle(
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(10.dp))

                // Afficher les données météorologiques ici
                Text(
                    text = "Température : ${weatherData.currentTemperature}°C",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = "Température max : ${weatherData.maxTemperature}°C",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = "Température min : ${weatherData.minTemperature}°C",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = "Temps : ${weatherData.weatherCondition}",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = "Vent : ${weatherData.windSpeed} m/s",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = "Indice UV : ${weatherData.uvIndex}",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
    }
}

