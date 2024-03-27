import android.util.Log
import com.example.appmeteo.android.WeatherDataModel
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class WeatherDataManager {

    companion object {
        private const val BASE_URL = "https://api.meteomatics.com/now"
        private const val USERNAME = "universitdelimoges_ferreira_logan"
        private const val PASSWORD = "7zpJX3vt9B"
        private const val NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/search"
    }

    suspend fun fetchWeather(cityName: String): WeatherDataModel {
        val weatherData = WeatherDataModel(0.0, 0.0, 0.0, "", 0.0, 0.0)
        try {
            val location = retrieveLocation(cityName)
            val latitude = location.getDouble("lat")
            val longitude = location.getDouble("lon")

            val temperatureUrl = "$BASE_URL/t_2m:C/$latitude,$longitude/json"
            val temperatureMinUrl = "$BASE_URL/t_min_2m_24h:C/$latitude,$longitude/json"
            val temperatureMaxUrl = "$BASE_URL/t_max_2m_24h:C/$latitude,$longitude/json"
            val weatherSymbolUrl = "$BASE_URL/weather_symbol_24h:idx/4$latitude,$longitude/json"
            val windSpeedUrl = "$BASE_URL/wind_speed_10m:ms/$latitude,$longitude/json"
            val uvIndexUrl = "$BASE_URL/uv:idx/$latitude,$longitude/json"

            val temperature = fetchData(temperatureUrl)
            val temperatureMin = fetchData(temperatureMinUrl)
            val temperatureMax = fetchData(temperatureMaxUrl)
            val weatherSymbol = fetchWeatherSymbol(weatherSymbolUrl)
            val windSpeed = fetchData(windSpeedUrl)
            val uvIndex = fetchData(uvIndexUrl)

            weatherData.currentTemperature = temperature
            weatherData.minTemperature = temperatureMin
            weatherData.maxTemperature = temperatureMax
            weatherData.weatherCondition = weatherSymbol
            weatherData.windSpeed = windSpeed
            weatherData.uvIndex = uvIndex
        } catch (e: Exception) {
            Log.e("WeatherApp", "Error fetching weather data", e)
        }
        return weatherData
    }

    private suspend fun retrieveLocation(cityName: String): JSONObject {
        val nominatimUrl = "$NOMINATIM_BASE_URL?format=json&q=${URLEncoder.encode(cityName, "UTF-8")}"
        val nominatimResponse = URL(nominatimUrl).readText()
        val jsonArray = JSONArray(nominatimResponse)
        return jsonArray.getJSONObject(0)
    }

    private suspend fun fetchData(urlString: String): Double {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val encodedAuth = android.util.Base64.encodeToString("$USERNAME:$PASSWORD".toByteArray(Charsets.UTF_8), android.util.Base64.NO_WRAP)
        connection.setRequestProperty("Authorization", "Basic $encodedAuth")
        connection.connect()

        val responseCode = connection.responseCode
        return if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val result = inputStream.bufferedReader().use { it.readText() }
            val jsonResponse = JSONObject(result)
            val data = jsonResponse.getJSONArray("data").getJSONObject(0)
            val coordinates = data.getJSONArray("coordinates").getJSONObject(0)
            val dates = coordinates.getJSONArray("dates").getJSONObject(0)
            dates.getDouble("value")
        } else {
            Log.e("WeatherApp", "HTTP error response code: $responseCode")
            0.0
        }
    }

    private suspend fun fetchWeatherSymbol(urlString: String): String {
        val response = fetchData(urlString)
        return getWeatherSymbol(response.toInt())
    }

    private fun getWeatherSymbol(symbolIdx: Int): String {
        return when (symbolIdx) {
            0 -> "Ensoleillé"
            1 -> "Partiellement Nuageux"
            2 -> "Majoritairement Nuageux"
            3 -> "Nuageux"
            4 -> "Voilé"
            5 -> "Averse faible"
            6 -> "Averse"
            7 -> "Chute de neige"
            8 -> "Orage"
            else -> "ERREUR"
        }
    }
}
