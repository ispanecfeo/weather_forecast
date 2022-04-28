package com.example.weather_forecast.repository.api


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.gb.kotlinapp.model.WeatherInfoDTO

interface WeatherAPI {

    @GET("v2/informers")
    fun getWeatherInfo(
        @Header("X-Yandex-API-Key") token: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ):Call<WeatherInfoDTO>

}