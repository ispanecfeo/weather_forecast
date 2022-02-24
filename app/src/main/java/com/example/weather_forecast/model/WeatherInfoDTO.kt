package ru.gb.kotlinapp.model

data class WeatherInfoDTO (
    val fact: FactInfoDTO?
)

data class FactInfoDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?
)