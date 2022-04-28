package com.example.weather_forecast.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityInfo(
    val city: String,
    val lat: Double,
    val lon: Double,
    val isRussian: Boolean = true
) : Parcelable

