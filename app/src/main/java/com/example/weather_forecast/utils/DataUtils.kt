package com.example.weather_forecast.utils

import com.example.weather_forecast.model.WeatherInfo
import com.example.weather_forecast.model.getCityByСoordinates
import com.example.weather_forecast.model.getDefaultCity
import ru.gb.kotlinapp.model.WeatherInfoDTO

fun convertDTOToModel(weatherInfo: WeatherInfoDTO, lat: Double, lon : Double) : WeatherInfo {
   val fact = weatherInfo.fact!!

    return WeatherInfo(
             getCityByСoordinates(lat, lon),
             fact.temp!!,
             fact.feels_like!!,
             fact.condition!!,
             fact.icon!!
          )
}

