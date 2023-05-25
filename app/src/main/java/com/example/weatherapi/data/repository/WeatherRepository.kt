package com.example.weatherapi.data.repository

import com.example.weatherapi.data.errorHandling.DataOrException
import com.example.weatherapi.data.model.Weather
import com.example.weatherapi.data.remote.WeatherApiRequest
import javax.inject.Inject

/*To allow getting the weather data from the WeatherAPI*/
class WeatherRepository @Inject constructor(private val api: WeatherApiRequest) {
    suspend fun getWeather(
        latQuery: Double,
        lonQuery: Double,
    ): DataOrException<Weather, Boolean, Exception> {
        val response = try {
            api.getWeather(lat = latQuery, lon = lonQuery)

        } catch (e: Exception) {
            return DataOrException(e = e)
        }
        return DataOrException(data = response)
    }
}