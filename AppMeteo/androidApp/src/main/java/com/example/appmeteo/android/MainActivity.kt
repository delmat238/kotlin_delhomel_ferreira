import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.example.appmeteo.android.WeatherDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val DEFAULT_CITY = "Paris" // Ville par défaut
    private var weatherData by mutableStateOf(WeatherDataModel())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Récupérer le nom de la ville sauvegardé dans les préférences partagées
        val savedCityName = sharedPreferences.getString("cityName", DEFAULT_CITY) ?: DEFAULT_CITY

        setContent {
            var cityName by remember { mutableStateOf(savedCityName) }

            Column {
                TextField(
                    value = cityName,
                    onValueChange = {
                        cityName = it
                        saveCityName(it) // Enregistrer le nom de la ville lorsqu'il est modifié
                    },
                    label = { Text("Nom de la ville") },
                    modifier = Modifier.padding(16.dp)
                )

                Button(
                    onClick = {
                        if (cityName.isNotBlank()) {
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
                    Text("Obtenir les données météorologiques")
                }

                WeatherDataDisplay(weatherData)
            }
        }
    }

    // Fonction pour enregistrer le nom de la ville dans les préférences partagées
    private fun saveCityName(cityName: String) {
        sharedPreferences.edit().putString("cityName", cityName).apply()
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
