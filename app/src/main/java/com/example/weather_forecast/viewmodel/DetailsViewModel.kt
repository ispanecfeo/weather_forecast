package com.example.weather_forecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather_forecast.repository.DetailsRepository
import com.example.weather_forecast.repository.DetailsRepositoryImpl
import com.example.weather_forecast.repository.RemoteDataSource
import com.example.weather_forecast.utils.convertDTOToModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.kotlinapp.model.WeatherInfoDTO

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepoImpl : DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    private val callback = object : Callback<WeatherInfoDTO> {

        override fun onResponse(call: Call<WeatherInfoDTO>, response: Response<WeatherInfoDTO>) {
            val serverResponse : WeatherInfoDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(
                        serverResponse,
                        detailsRepoImpl.lat,
                        detailsRepoImpl.lan
                    )
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherInfoDTO>, t: Throwable) {
            detailsLiveData.postValue(
                AppState.Error(
                    Throwable(t.message ?: REQUEST_ERROR)
                )
            )
        }

    }

    private fun checkResponse(serverResponse: WeatherInfoDTO, lat: Double, lon: Double): AppState {
        val fact = serverResponse.fact
        return if (fact != null) {
             if (fact.temp == null
                || fact.feels_like == null
                || fact.icon.isNullOrEmpty()
                || fact.condition.isNullOrEmpty())
            {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.SuccessLoadFromServer(
                    convertDTOToModel(
                        serverResponse,
                        lat,
                        lon
                    )
                )
            }
        } else {
            AppState.Error(Throwable(CORRUPTED_DATA))
        }
    }

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.postValue(AppState.Loading)
        detailsRepoImpl.getWeatherInfo(lat, lon, callback)
    }

}