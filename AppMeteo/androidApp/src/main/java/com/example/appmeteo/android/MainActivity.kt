package com.example.appmeteo.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmeteo.android.view.App

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navControler = rememberNavController()
                val routeHome = "Home"
                val routeFavoris = "Favoris"
                Scaffold(
//                    topBar = {
//                        TopAppBar(
//                            title = { Text("App Meteo") }
//                        )
//
//                    },
                    content = { innerPadding ->

                        NavHost(
                            navController = navControler, startDestination = "Home"
                        ) {
                            composable(
                                route = "Home",
                            ) {
                                App(applicationContext, innerPadding)
                            }
                            composable(
                                route = "Favoris",
                            ) {
                                App(applicationContext, innerPadding)
                            }

                        }
                    },

                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(selected = pageSelectione(navControler, "Home"),
                                onClick = { navControler.navigate("Home") },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Home, contentDescription = null
                                    )
                                },
                                label = { Text(text = "Home") })

                            NavigationBarItem(selected = false,
                                onClick = { navControler.navigate("Favoris") },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Star, contentDescription = null
                                    )
                                },
                                label = {
                                    Text(text = "Favoris")


                                }

                            )
//                Surface(Modifier.fillMaxSize()) {
//                    App()

                        }


                    })
            }
        }

    }

}

fun pageSelectione(navControler: NavHostController, s: String): Boolean {
//    if (navControler.currentDestination.toString().equals(s)) {
//        return true
//    } else {
//        return false
//    }

    navControler.currentDestination?.let { println(it.equals(s)) }
    return true
}

