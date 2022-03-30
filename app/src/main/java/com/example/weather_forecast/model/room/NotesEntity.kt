package com.example.weather_forecast.model.room

import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lon"])
data class NotesEntity(
    var lat : Double = 0.0,
    var lon : Double = 0.0,
    var comment : String = ""
)