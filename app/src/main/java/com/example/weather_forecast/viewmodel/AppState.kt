package com.example.weather_forecast.viewmodel

import com.example.weather_forecast.model.CityInfo
import com.example.weather_forecast.model.HistoryCity
import com.example.weather_forecast.model.WeatherInfo
import com.example.weather_forecast.model.room.WeatherEntity

sealed class AppState {
    data class Success(val citiesData: List<CityInfo>) : AppState()
    data class SuccessLoadFromServer(val weatherData: WeatherInfo) : AppState()
    data class SuccessHistoryData(val data: List<HistoryCity>?) : AppState()
    data class SuccessHistoryWeather(val data: List<WeatherEntity>?) : AppState()
    data class Error(val error : Throwable) : AppState()
    object Loading:AppState()
}
