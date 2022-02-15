package com.example.weather_forecast.model

interface Repo {
    fun getWeatherFromServer(): WeatherInfo
    fun getWeatherFromLocalStorageRus(): List<WeatherInfo>
    fun getWeatherFromLocalStorageWorld(): List<WeatherInfo>
}