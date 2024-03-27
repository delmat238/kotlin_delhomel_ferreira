package com.example.appmeteo.android

data class WeatherDataModel(
    var currentTemperature: Double = 0.0,
    var minTemperature: Double = 0.0,
    var maxTemperature: Double = 0.0,
    var weatherCondition: String = "",
    var windSpeed: Double = 0.0,
    var uvIndex: Double = 0.0
)
