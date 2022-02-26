package com.example.weather_forecast.model

class RepoImpl:Repo {
    override fun getWeatherFromServer() = WeatherInfo()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}
