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
            liveDataToObserve.postValue(
                AppState.Success(repoImpl.getWeatherFromLocalStorage())
            )
        }.start()
    }

}