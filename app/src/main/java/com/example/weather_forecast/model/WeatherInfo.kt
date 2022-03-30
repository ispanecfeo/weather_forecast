package com.example.weather_forecast.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherInfo(
    val cityInfo: CityInfo = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0,
    val condition: String = "ясно",
    val icon: String = "bkn_n"
) : Parcelable


fun getDefaultCity() = CityInfo(
    city = "Москва",
    lat = 55.755826,
    lon = 37.617299900000035
)

fun getCityByСoordinates(lat: Double, lon: Double): CityInfo {

    var cityInfo: CityInfo? = getRussianCities().find {
        it.lat == lat && it.lon == lon
    }

    if (cityInfo == null) {
        cityInfo = getWorldCities().find {
            it.lat == lat && it.lon == lon
        }
    }

    return cityInfo ?: getDefaultCity()
}


fun getWorldCities() = listOf(
    CityInfo("Лондон", 51.5085300, -0.1257400, false),
    CityInfo("Токио", 35.6895000, 139.6917100, false),
    CityInfo("Париж", 48.8534100, 2.3488000, false),
    CityInfo("Берлин", 52.52000659999999, 13.404953999999975, false),
    CityInfo("Рим", 41.9027835, 12.496365500000024, false),
    CityInfo("Минск", 53.90453979999999, 27.561524400000053, false),
    CityInfo("Стамбул", 41.0082376, 28.97835889999999, false),
    CityInfo("Вашингтон", 38.9071923, -77.03687070000001, false),
    CityInfo("Киев", 50.4501, 30.523400000000038, false),
    CityInfo("Пекин", 39.90419989999999, 116.40739630000007, false)
)

fun getRussianCities() = listOf(
    CityInfo("Москва", 55.755826, 37.617299900000035),
    CityInfo("Санкт-Петербург", 59.9342802, 30.335098600000038),
    CityInfo("Новосибирск", 55.00835259999999, 82.93573270000002),
    CityInfo("Екатеринбург", 56.83892609999999, 60.60570250000001),
    CityInfo("Нижний Новгород", 56.2965039, 43.936059),
    CityInfo("Казань", 55.8304307, 49.06608060000008),
    CityInfo("Челябинск", 55.1644419, 61.4368432),
    CityInfo("Омск", 54.9884804, 73.32423610000001),
    CityInfo("Ростов-на-Дону", 47.2357137, 39.701505),
    CityInfo("Уфа", 54.7387621, 55.972055400000045)
)
