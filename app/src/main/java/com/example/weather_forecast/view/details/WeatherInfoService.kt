package com.example.weather_forecast.view.details

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather_forecast.BuildConfig
import com.example.weather_forecast.model.WeatherInfo
import com.google.gson.Gson
import ru.gb.kotlinapp.model.FactInfoDTO
import ru.gb.kotlinapp.model.WeatherInfoDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val WEATHER_INFO_EXTRA = "WEATHER_INFO_EXTRA"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
private const val REQUEST_API_KEY = "X-Yandex-API-Key"

class WeatherInfoService : IntentService("WeatherInfoService") {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val weatherInfoBundle = it.getParcelableExtra<WeatherInfo>(WEATHER_INFO_EXTRA)
            weatherInfoBundle?.let { wi->
                getWeather(
                    wi.cityInfo.lat.toString(),
                    wi.cityInfo.lon.toString()
                )
            } ?: run {
                onEmptyIntent()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getWeather(lat: String, lon:String) {
        try {
            val uri = URL(
                "https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}"
            )

            lateinit var urlConnection : HttpsURLConnection

            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                    addRequestProperty(
                        REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY
                    )
                    val weatherInfoDTO : WeatherInfoDTO =
                        Gson().fromJson(
                            getLines(
                                BufferedReader(InputStreamReader(urlConnection.inputStream))
                            ),
                            WeatherInfoDTO::class.java
                        )
                    onResponse(weatherInfoDTO)
                }

            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }


        } catch (e: MalformedURLException) {
            onMalformedURL()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onResponse(weatherDTO: WeatherInfoDTO) {
        val fact = weatherDTO.fact

        fact?.let{
            onSuccessResponse(weatherDTO)
        } ?: run {
            onEmptyResponse()
        }
    }

    private fun onSuccessResponse(fact:WeatherInfoDTO) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_FACT_EXTRA, fact)
        sendBroadcast()
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        sendBroadcast()
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        sendBroadcast()
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        sendBroadcast()
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        sendBroadcast()
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }

    private fun sendBroadcast() {
        LocalBroadcastManager
            .getInstance(this)
            .sendBroadcast(broadcastIntent)
    }

}