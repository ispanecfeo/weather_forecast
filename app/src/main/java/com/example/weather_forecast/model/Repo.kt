package com.example.weather_forecast.model

interface Repo {
    fun getWeatherFromServer(): WeatherInfo
    fun getWeatherFromLocalStorage(): WeatherInfo
}