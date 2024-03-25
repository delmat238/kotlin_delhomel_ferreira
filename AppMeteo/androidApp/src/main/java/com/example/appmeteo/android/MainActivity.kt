package com.example.appmeteo.android

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.data.EmptyGroup.location
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(Modifier.fillMaxSize()) {
                    SearchBar()

                }

            }
        }
    }
}


@Composable
fun SearchBar() {
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

            Button(
                onClick = { validerVille(text) }
            ) {
                Text("Valider")
            }
            Button(onClick = {
                // Déclencher l'opération de géolocalisation
                scope.launch {
                    val retrievedLocation = retrieveLocation(LocalContext.current)
                    location = retrievedLocation
                    // Appeler la fonction de rappel avec la localisation récupérée
                    onLocationClicked(retrievedLocation)
                }
            }) {
                Text("Utiliser géolocalisation")
            }

        }

    }
}

fun geolocalisation(any: Any) {

}

@Composable
fun Geolocalisation(location: Any) {
    TODO("Not yet implemented")
}


fun validerVille(text: String) {
    println("coucou !")
}


