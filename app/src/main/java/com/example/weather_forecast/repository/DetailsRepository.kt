package com.example.weather_forecast.repository

import retrofit2.Callback
import ru.gb.kotlinapp.model.WeatherInfoDTO

interface DetailsRepository {

    var lat: Double
    var lan : Double

    fun getWeatherInfo(
        lat: Double,
        lan: Double,
        callback: Callback<WeatherInfoDTO>
    )
}