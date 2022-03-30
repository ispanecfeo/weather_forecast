package com.example.weather_forecast.model.room

import androidx.room.Entity
import java.util.*

@Entity(primaryKeys = ["lat", "lon", "date"])
data class WeatherEntity(
    var lat : Double = 0.0,
    var lon : Double = 0.0,
    var date: Date,
    var temperature : Int = 0,
    var feelsLike: Int = 0,
    var condition: String = ""
)
