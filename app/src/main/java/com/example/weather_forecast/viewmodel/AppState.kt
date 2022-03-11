package com.example.weather_forecast.viewmodel

import com.example.weather_forecast.model.WeatherInfo

sealed class AppState {
    data class Success(val weatherData: List<WeatherInfo>) : AppState()
    data class SuccessLoadFromServer(val weatherData: WeatherInfo) : AppState()
    data class Error(val error : Throwable) : AppState()
    object Loading:AppState()
}
