package com.example.weather_forecast.repository

import com.example.weather_forecast.repository.remote.RemoteDataSource
import retrofit2.Callback
import ru.gb.kotlinapp.model.WeatherInfoDTO

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {
    override var lat : Double = 55.755826
    override var lan: Double = 37.617299900000035

    override fun getWeatherInfo(lat: Double, lan: Double, callback: Callback<WeatherInfoDTO>) {
        this.lat = lat
        this.lan = lan
        remoteDataSource.getWeatherDetails(lat, lan, callback)
    }

}