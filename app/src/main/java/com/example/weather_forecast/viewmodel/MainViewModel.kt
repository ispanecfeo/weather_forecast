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

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(false)
    fun getWeatherFromRemoteSource() = getDataFromLocalSource(true)

    private fun getDataFromLocalSource(isRussian:Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread{
            sleep(1000)
            liveDataToObserve.postValue(
                AppState.Success(
                    if (isRussian)
                        repoImpl.getWeatherFromLocalStorageRus()
                    else
                        repoImpl.getWeatherFromLocalStorageWorld()
                )
            )
        }.start()
    }

}