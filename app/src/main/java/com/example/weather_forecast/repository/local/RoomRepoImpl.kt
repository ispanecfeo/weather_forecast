package com.example.weather_forecast.repository.local

import com.example.weather_forecast.App
import com.example.weather_forecast.model.HistoryCity
import com.example.weather_forecast.model.WeatherInfo
import com.example.weather_forecast.model.room.WeatherEntity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RoomRepoImpl : RoomRepo {

    private val dao = App.getWeatherInfoDao()

    override suspend fun insertHistoryWeather(weatherInfo: WeatherInfo) {

        dao?.insertWeatherInfo(WeatherEntity(
            weatherInfo.cityInfo.lat,
            weatherInfo.cityInfo.lon,
            Date(),
            weatherInfo.temperature,
            weatherInfo.feelsLike,
            weatherInfo.condition
        ))
    }

    override suspend fun getHistoryCities(city: String): List<HistoryCity>? {
        return dao?.getHistoryCities(city)
    }

    override suspend fun getHistoryWeatherByCoordinates(
        lat: Double,
        lon: Double,
        aboveZero: Boolean
    ): List<WeatherEntity>? {
        return if (aboveZero)
                dao?.getAllWeatherInfoByCityAboveZero(lat, lon)
            else
                dao?.getAllWeatherInfoByCity(lat, lon)
    }
}