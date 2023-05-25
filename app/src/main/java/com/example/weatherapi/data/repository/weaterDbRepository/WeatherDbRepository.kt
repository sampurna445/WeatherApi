package com.example.weatherapi.data.repository.weaterDbRepository

import com.example.weatherapi.data.database.CurrentWeatherObject
import com.example.weatherapi.data.database.Favourite
import com.example.weatherapi.data.database.WeatherDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WeatherDbRepository @Inject constructor(private val weatherDao: WeatherDao) {

    fun getFavourites(): Flow<List<Favourite>> = weatherDao.getFavourites()
    suspend fun insertFavourite(favourite: Favourite) = weatherDao.insertFavourite(favourite)
    suspend fun updateFavourite(favourite: Favourite) = weatherDao.updateFavourite(favourite)
    suspend fun deleteAllFavourite() = weatherDao.deleteAllFavourites()
    suspend fun deleteFavourite(favourite: Favourite) = weatherDao.deleteFavourite(favourite)
    suspend fun getFavById(city: String): Favourite = weatherDao.getFavById(city)

    // Current weather table
    fun getWeatherObjects(): Flow<List<CurrentWeatherObject>> = weatherDao.getWeatherObjects()
    suspend fun getWeatherById(id: Int): CurrentWeatherObject = weatherDao.getWeatherById(id)
    suspend fun insertCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) = weatherDao.insertCurrentWeatherObject(currentWeatherObject)
    suspend fun updateCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) = weatherDao.updateCurrentWeatherObject(currentWeatherObject)
}