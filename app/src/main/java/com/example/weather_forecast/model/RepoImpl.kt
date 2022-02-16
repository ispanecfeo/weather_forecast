package com.example.weather_forecast.model

class RepoImpl:Repo {
    override fun getWeatherFromServer(): WeatherInfo {
        return WeatherInfo()
    }

    override fun getWeatherFromLocalStorage(): WeatherInfo {
        return WeatherInfo()
    }
}