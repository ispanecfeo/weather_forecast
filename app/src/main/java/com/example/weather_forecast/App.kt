package com.example.weather_forecast

import android.app.Application
import androidx.room.Room
import com.example.weather_forecast.model.room.WeatherDAO
import com.example.weather_forecast.model.room.WeatherInfoDataBase
import java.lang.IllegalStateException

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: WeatherInfoDataBase? = null
        private const val DB_NAME = "base_weather.db"
        public const val NUMBER_FRAGMENT : String = "FRAGMENT_NUMBER"

        fun getWeatherInfoDao(): WeatherDAO? {
            synchronized(WeatherInfoDataBase::class.java) {
                if (db == null) {
                    if (appInstance == null) throw IllegalStateException("Application must not be null")

                    db = Room.databaseBuilder(
                        appInstance!!.applicationContext,
                        WeatherInfoDataBase::class.java,
                        DB_NAME
                    )
                        .build()
                }
            }
            return db?.weatherInfoDao()
        }
    }

}