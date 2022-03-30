package com.example.weather_forecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_forecast.model.CityInfo
import com.example.weather_forecast.model.Repo
import com.example.weather_forecast.model.RepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repoImpl: Repo = RepoImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus(fromRoom: Boolean) = getDataFromLocalSource(true, fromRoom)
    fun getWeatherFromLocalSourceWorld(fromRoom: Boolean) = getDataFromLocalSource(false, fromRoom)
    fun setCitiesIntoDB() = insertCitiesIntoDB()


    private fun getDataFromLocalSource(isRussian: Boolean, fromRoom: Boolean) {

        viewModelScope.launch(Dispatchers.IO) {

            liveDataToObserve.postValue(AppState.Loading)
            liveDataToObserve.postValue(
                AppState.Success(
                    if (fromRoom)
                        repoImpl.getCitiesFromDb(if (isRussian) 1 else 0)
                    else
                        repoImpl.getCitiesFromMemory(if (isRussian) 1 else 0)
                )
            )

        }
    }

    private fun insertCitiesIntoDB() {
        viewModelScope.launch(Dispatchers.IO) {
            repoImpl.insertCitiesToDb(repoImpl.getAllCitiesFromMemory())
        }
    }

}