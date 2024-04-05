import android.util.Log
import com.example.appmeteo.android.WeatherDataModel
import com.example.appmeteo.android.WeatherDataParser
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

            val combinedUrl = "$BASE_URL/t_2m:C,t_min_2m_24h:C,t_max_2m_24h:C," +
                    "weather_symbol_1h:idx,wind_speed_10m:ms,uv:idx/" +
                    "$latitude,$longitude/json"

            val responseData = fetchData(combinedUrl)
            Log.d("WeatherApp", "Response JSON: $responseData")

            return WeatherDataParser.parseWeatherData(responseData)

        } catch (e: Exception) {
            Log.e("WeatherApp", "Error fetching weather data", e)
        }
        return weatherData
    }

    private suspend fun retrieveLocation(cityName: String): JSONObject {
        return withContext(Dispatchers.IO) {
            val nominatimUrl =
                "$NOMINATIM_BASE_URL?format=json&q=${URLEncoder.encode(cityName, "UTF-8")}"
            val nominatimResponse = URL(nominatimUrl).readText()
            val jsonArray = JSONArray(nominatimResponse)
            jsonArray.getJSONObject(0)
        }
    }

    private suspend fun fetchData(urlString: String): JSONObject {
        return withContext(Dispatchers.IO) {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val encodedAuth = android.util.Base64.encodeToString(
                "$USERNAME:$PASSWORD".toByteArray(Charsets.UTF_8),
                android.util.Base64.NO_WRAP
            )
            connection.setRequestProperty("Authorization", "Basic $encodedAuth")
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val result = inputStream.bufferedReader().use { it.readText() }
                JSONObject(result)
            } else {
                Log.e("WeatherApp", "HTTP error response code: $responseCode")
                JSONObject()
            }
        }
    }
}