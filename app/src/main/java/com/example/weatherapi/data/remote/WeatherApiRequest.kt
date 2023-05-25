package com.example.weatherapi.data.remote

import com.example.weatherapi.data.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiRequest {
    @GET(WeatherApiDetails.WEATHER_ENDPOINT)
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely",
        @Query("appid") appid: String = WeatherApiDetails.API_KEY
    ): Weather
}