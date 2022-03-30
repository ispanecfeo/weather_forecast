package com.example.weather_forecast.utils.room

import androidx.room.TypeConverter
import java.util.*

class Converter {

    companion object {

        @TypeConverter
        @JvmStatic
        fun toDate(value: Long): Date {
            return Date(value)
        }

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date): Long {
            return date.time
        }

    }

}