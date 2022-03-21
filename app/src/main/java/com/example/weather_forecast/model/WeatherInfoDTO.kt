package ru.gb.kotlinapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherInfoDTO (
    val fact: FactInfoDTO?
): Parcelable

@Parcelize
data class FactInfoDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?,
    val icon: String?
): Parcelable