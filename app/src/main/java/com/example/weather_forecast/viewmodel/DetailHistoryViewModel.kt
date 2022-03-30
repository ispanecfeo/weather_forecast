package com.example.weather_forecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_forecast.repository.local.RoomRepo
import com.example.weather_forecast.repository.local.RoomRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailHistoryViewModel(
    private val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository:RoomRepo = RoomRepoImpl()
): ViewModel() {

    fun getLiveData() = historyLiveData
    fun getHistoryData(lat: Double, lon:Double, aboveZero: Boolean) = getHistoryDataFromDB(lat, lon, aboveZero)

    private fun getHistoryDataFromDB(lat: Double, lon: Double, aboveZero: Boolean) {

        viewModelScope.launch(Dispatchers.IO) {
            historyLiveData.postValue(AppState.Loading)
            historyLiveData.postValue(
                AppState.SuccessHistoryWeather(
                    repository.getHistoryWeatherByCoordinates(
                        lat,
                        lon,
                        aboveZero
                    )
                )
            )
        }

    }

}