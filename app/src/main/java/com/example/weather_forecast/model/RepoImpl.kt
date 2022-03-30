package com.example.weather_forecast.model

import com.example.weather_forecast.App
import com.example.weather_forecast.model.room.CityEntity

class RepoImpl : Repo {

    private val dao = App.getWeatherInfoDao()

    override fun getWeatherFromServer() = WeatherInfo()

    override suspend fun getCitiesFromMemory(isRussian: Int): List<CityInfo> {
        return if (isRussian == 1)
            getRussianCities()
        else
            getWorldCities()
    }

    override suspend fun getCitiesFromDb(isRussian: Int): List<CityInfo> {
        return dao?.getAllCities(isRussian)?.map { CityInfo(it.city, it.lat, it.lon, it.isRussian == 1) }
            ?: arrayListOf(getDefaultCity())
    }

    override suspend fun insertCitiesToDb(cities: List<CityInfo>) {
        dao?.insertCities(cities.map { CityEntity(it.lat, it.lon, if (it.isRussian) 1 else 0, it.city) })
    }

    override suspend fun getAllCitiesFromMemory(): List<CityInfo> {
       return getRussianCities() + getWorldCities()
    }

}
