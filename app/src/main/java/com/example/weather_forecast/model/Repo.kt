package com.example.weather_forecast.model

interface Repo {
    fun getWeatherFromServer(): WeatherInfo
    suspend fun getCitiesFromMemory(isRussian: Int): List<CityInfo>
    suspend fun getCitiesFromDb(isRussian: Int): List<CityInfo>
    suspend fun insertCitiesToDb(cities :List<CityInfo>)
    suspend fun getAllCitiesFromMemory(): List<CityInfo>
}