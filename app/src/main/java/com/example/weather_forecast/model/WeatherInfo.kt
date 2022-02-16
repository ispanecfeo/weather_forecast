package com.example.weather_forecast.model

data class WeatherInfo(
    val cityInfo: CityInfo = getDefaultCity(),
    val temperature:Int = 0,
    val condition: String = "ясно"
)


fun getDefaultCity() = CityInfo(
    city = "Москва",
    lat = 55.755826,
    lon = 37.617299900000035
)