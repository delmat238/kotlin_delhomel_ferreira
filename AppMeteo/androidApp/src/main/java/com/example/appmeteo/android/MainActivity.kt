package com.example.appmeteo.android

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(Modifier.fillMaxSize()) {
                    App()

                }

            }
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current

    SearchBar(context)
}



@Composable
fun SearchBar(context: Context) {
    var text by remember {
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.Top, // Centre verticalement tout le contenu de la colonne
        horizontalAlignment = Alignment.CenterHorizontally, // Centre horizontalement tout le contenu de la colonne
        modifier = Modifier.fillMaxWidth() // Utilise toute la largeur disponible
    ) {

        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            singleLine = true,
            modifier = Modifier.padding(10.dp)
        )

        Row {

            Button(onClick = { validerVille(text) }) {
                Text("Valider")
            }
            Button(onClick = {btnLocalisation(context)}) {
                Text("Utiliser géolocalisation")
            }

            val hasLocationPermission by remember {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                )
            }


            if (hasLocationPermission) {
                Text("Permission de localisation accordée")
            } else {
                Text("Permission de localisation non accordée")
            }

        }

    }
}

@SuppressLint("MissingPermission")
fun btnLocalisation(context: Context) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    println()
    println("avant le if")
    if (locationManager != null) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

fun validerVille(text: String) {
    println(text)
}



