package com.example.weather_forecast.model.room

import androidx.room.*
import com.example.weather_forecast.model.HistoryCity


@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCity(city: CityEntity)

    @Delete
    suspend fun deleteCity(city: CityEntity)

    @Query("DELETE FROM CityEntity")
    suspend fun deleteAllCities()

    @Query("SELECT * FROM CityEntity WHERE isRussian = :isRussian")
    suspend fun getAllCities(isRussian : Int): List<CityEntity>

    @Query("SELECT * FROM CityEntity WHERE city LIKE :city")
    fun getCitiesByName(city: String): List<CityEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherInfo(weatherInfo: WeatherEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWeatherInfo(weatherInfo: WeatherEntity)

    @Delete
    suspend fun deleteWeatherInfo(weatherInfo: WeatherEntity)

    @Query("DELETE FROM WeatherEntity")
    suspend fun deleteAllWeatherInfo()

    @Query("SELECT * FROM WeatherEntity WHERE lat = :lat AND lon = :lon ORDER BY date")
    suspend fun getAllWeatherInfoByCity(lat:Double, lon:Double) : List<WeatherEntity>

    @Query("SELECT * FROM WeatherEntity WHERE lat = :lat AND lon = :lon AND temperature > 0 ORDER BY date")
    suspend fun getAllWeatherInfoByCityAboveZero(lat:Double, lon:Double) : List<WeatherEntity>

    @Query(
        "SELECT DISTINCT " +
             "CityEntity.city AS city, " +
             "CityEntity.lat AS lat, " +
             "CityEntity.lon AS lon " +
             "FROM CityEntity INNER JOIN WeatherEntity "  +
             "ON CityEntity.lat = WeatherEntity.lat AND CityEntity.lon = WeatherEntity.lon AND city LIKE :city"
    )
    suspend fun getHistoryCities(city : String): List<HistoryCity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NotesEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWeatherInfo(note: NotesEntity)

    @Delete
    suspend fun deleteWeatherInfo(note: NotesEntity)

    @Query("DELETE FROM NotesEntity")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM NotesEntity WHERE lat = :lat AND lon = :lon")
    suspend fun getNotesByCoordinates(lat:Double, lon:Double) : List<NotesEntity>

}