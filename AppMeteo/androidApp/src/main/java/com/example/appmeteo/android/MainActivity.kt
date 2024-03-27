package com.example.appmeteo.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmeteo.android.view.App
import com.example.appmeteo.android.view.Favoris
import com.example.appmeteo.android.view.Prevision

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
                var selectedTab by remember { mutableStateOf("Home") }
                Scaffold(
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
                                Favoris()
                            }
                            composable(
                                route = "Prevision",
                            ) {
                                Prevision()
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


                    })
            }
        }

    }


}

fun selectionneIcon(selectedTab: String, r: String): Boolean {
    return selectedTab == r
}
