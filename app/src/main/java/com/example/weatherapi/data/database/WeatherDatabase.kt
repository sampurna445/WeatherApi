package com.example.weatherapi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Favourite::class, CurrentWeatherObject::class], version = 2, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}