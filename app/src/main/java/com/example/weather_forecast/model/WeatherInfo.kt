package com.example.weather_forecast.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherInfo(
    val cityInfo: CityInfo = getDefaultCity(),
    val temperature:Int = 0,
    val feelsLike: Int = 0,
    val condition: String = "ясно",
    val icon : String = "bkn_n"
) : Parcelable


fun getDefaultCity() = CityInfo(
    city = "Москва",
    lat = 55.755826,
    lon = 37.617299900000035
)

fun getCityByСoordinates(lat: Double, lon:Double) : CityInfo {

    var weatherInfo:WeatherInfo? = getRussianCities().find {
        it.cityInfo.lat == lat && it.cityInfo.lon == lon
    }

    if (weatherInfo == null) {
        weatherInfo = getWorldCities().find {
            it.cityInfo.lat == lat && it.cityInfo.lon == lon
        }
    }

    return  weatherInfo?.let {
        it.cityInfo
    } ?: getDefaultCity()
}


fun getWorldCities()= listOf(
        WeatherInfo(CityInfo("Лондон", 51.5085300, -0.1257400), 1, 2, "пасмурно"),
        WeatherInfo(CityInfo("Токио", 35.6895000, 139.6917100), 3, 4, "ветрено"),
        WeatherInfo(CityInfo("Париж", 48.8534100, 2.3488000), 5, 6),
        WeatherInfo(CityInfo("Берлин", 52.52000659999999, 13.404953999999975), 7, 8, "пасмурно"),
        WeatherInfo(CityInfo("Рим", 41.9027835, 12.496365500000024), 9, 10),
        WeatherInfo(CityInfo("Минск", 53.90453979999999, 27.561524400000053), 11, 12),
        WeatherInfo(CityInfo("Стамбул", 41.0082376, 28.97835889999999), 13, 14),
        WeatherInfo(CityInfo("Вашингтон", 38.9071923, -77.03687070000001), 15, 16),
        WeatherInfo(CityInfo("Киев", 50.4501, 30.523400000000038), 17, 18),
        WeatherInfo(CityInfo("Пекин", 39.90419989999999, 116.40739630000007), 19, 20)
)

fun getRussianCities() = listOf(
        WeatherInfo(CityInfo("Москва", 55.755826, 37.617299900000035), 1, 2, "пасмурно"),
        WeatherInfo(CityInfo("Санкт-Петербург", 59.9342802, 30.335098600000038), 3, 3, "облачно"),
        WeatherInfo(CityInfo("Новосибирск", 55.00835259999999, 82.93573270000002), 5, 6, "снежно"),
        WeatherInfo(CityInfo("Екатеринбург", 56.83892609999999, 60.60570250000001), 7, 8, "дождливо"),
        WeatherInfo(CityInfo("Нижний Новгород", 56.2965039, 43.936059), 9, 10),
        WeatherInfo(CityInfo("Казань", 55.8304307, 49.06608060000008), 11, 12),
        WeatherInfo(CityInfo("Челябинск", 55.1644419, 61.4368432), 13, 14),
        WeatherInfo(CityInfo("Омск", 54.9884804, 73.32423610000001), 15, 16),
        WeatherInfo(CityInfo("Ростов-на-Дону", 47.2357137, 39.701505), 17, 18),
        WeatherInfo(CityInfo("Уфа", 54.7387621, 55.972055400000045), 19, 20)
)
