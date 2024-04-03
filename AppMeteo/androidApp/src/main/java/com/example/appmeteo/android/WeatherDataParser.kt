package com.example.appmeteo.android
import org.json.JSONObject

class WeatherDataParser {

    companion object {

        fun parseWeatherData(responseData: JSONObject): WeatherDataModel {
            val weatherData = WeatherDataModel(0.0, 0.0, 0.0, "", 0.0, 0.0)

            // Extraction de la température actuelle
            val currentTempData = responseData.getJSONArray("data").getJSONObject(0)
            val currentTemperature = currentTempData.getJSONArray("coordinates")
                .getJSONObject(0).getJSONArray("dates").getJSONObject(0).getDouble("value")

            // Extraction de la température minimale
            val minTempData = responseData.getJSONArray("data").getJSONObject(1)
            val minTemperature = minTempData.getJSONArray("coordinates")
                .getJSONObject(0).getJSONArray("dates").getJSONObject(0).getDouble("value")

            // Extraction de la température maximale
            val maxTempData = responseData.getJSONArray("data").getJSONObject(2)
            val maxTemperature = maxTempData.getJSONArray("coordinates")
                .getJSONObject(0).getJSONArray("dates").getJSONObject(0).getDouble("value")

            // Extraction du symbole météorologique
            val weatherSymbolData = responseData.getJSONArray("data").getJSONObject(3)
            val weatherSymbolIdx = weatherSymbolData.getJSONArray("coordinates")
                .getJSONObject(0).getJSONArray("dates").getJSONObject(0).getInt("value")
            val weatherSymbol = getWeatherSymbol(weatherSymbolIdx)

            // Extraction de la vitesse du vent
            val windSpeedData = responseData.getJSONArray("data").getJSONObject(4)
            val windSpeed = windSpeedData.getJSONArray("coordinates")
                .getJSONObject(0).getJSONArray("dates").getJSONObject(0).getDouble("value")

            // Extraction de l'indice UV
            val uvIndexData = responseData.getJSONArray("data").getJSONObject(5)
            val uvIndex = uvIndexData.getJSONArray("coordinates")
                .getJSONObject(0).getJSONArray("dates").getJSONObject(0).getDouble("value")

            // Attribution des valeurs extraites à l'objet WeatherDataModel
            weatherData.currentTemperature = currentTemperature
            weatherData.minTemperature = minTemperature
            weatherData.maxTemperature = maxTemperature
            weatherData.weatherCondition = weatherSymbol
            weatherData.windSpeed = windSpeed
            weatherData.uvIndex = uvIndex

            return weatherData
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
}