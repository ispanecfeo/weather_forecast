package com.example.weather_forecast.repository.local

import com.example.weather_forecast.model.HistoryCity
import com.example.weather_forecast.model.WeatherInfo
import com.example.weather_forecast.model.room.WeatherEntity

interface RoomRepo {

    suspend fun insertHistoryWeather(weatherInfo: WeatherInfo)
    suspend fun getHistoryCities(city: String): List<HistoryCity>?
    suspend fun getHistoryWeatherByCoordinates(lat:Double, lon:Double, aboveZero: Boolean) : List<WeatherEntity>?

}