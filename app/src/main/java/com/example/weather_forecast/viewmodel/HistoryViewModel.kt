package com.example.weather_forecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_forecast.repository.local.RoomRepo
import com.example.weather_forecast.repository.local.RoomRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HistoryViewModel(
    private val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository:RoomRepo = RoomRepoImpl()
) : ViewModel() {

    fun getLiveData() = historyLiveData
    fun getHistoryData(city: String) = getHistoryDataFromDB(city)

    private fun getHistoryDataFromDB(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            historyLiveData.postValue(AppState.Loading)
            historyLiveData.postValue(
                AppState.SuccessHistoryData(repository.getHistoryCities(city))
            )
        }
    }

}

