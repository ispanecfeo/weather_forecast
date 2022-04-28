package com.example.weather_forecast.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather_forecast.utils.room.Converter

@Database(entities = [CityEntity::class, WeatherEntity::class, NotesEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class WeatherInfoDataBase : RoomDatabase() {
    abstract fun weatherInfoDao() : WeatherDAO
}