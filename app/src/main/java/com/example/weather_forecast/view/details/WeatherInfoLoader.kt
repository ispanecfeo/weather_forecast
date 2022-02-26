package com.example.weather_forecast.view.details

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weather_forecast.BuildConfig
import com.google.gson.Gson
import ru.gb.kotlinapp.model.WeatherInfoDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

@RequiresApi(Build.VERSION_CODES.N)
class WeatherInfoLoader (private val listener: WeatherInfoLoaderListener,
                         private val lat: Double,
                         private val lon: Double) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather() =
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}")
            val handler = Handler()

            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000

                        addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY )
                    }

                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    val response = getLines(bufferedReader)

                    val weatherDTO: WeatherInfoDTO =
                        Gson().fromJson(response, WeatherInfoDTO::class.java)

                    handler.post {
                        listener.onLoaded(weatherDTO)
                    }
                } catch (e: Exception) {
                    Log.e("", "Problem with connect", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    interface WeatherInfoLoaderListener {
        fun onLoaded(weatherInfoDTO: WeatherInfoDTO)
        fun onFailed(throwable: Throwable)
    }
}