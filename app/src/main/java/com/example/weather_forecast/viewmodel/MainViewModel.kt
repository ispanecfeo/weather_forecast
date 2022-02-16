package com.example.weather_forecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather_forecast.model.Repo
import com.example.weather_forecast.model.RepoImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve:MutableLiveData<AppState> = MutableLiveData(),
    private val repoImpl:Repo = RepoImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSource() = getDataFromLocalSource()
    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread{
            sleep(2000)
            val randNumber = (1..3).random()
            when(randNumber) {
                1 -> {
                    liveDataToObserve.postValue(
                        AppState.Success(repoImpl.getWeatherFromLocalStorage()))
                }
                2 -> {
                    liveDataToObserve.postValue(
                        AppState.Error(Exception("Ошибка связи с сервером"))
                    )
                }
                3 -> {
                    liveDataToObserve.postValue(
                        AppState.Loading
                    )
                }
            }
        }.start()
    }

}