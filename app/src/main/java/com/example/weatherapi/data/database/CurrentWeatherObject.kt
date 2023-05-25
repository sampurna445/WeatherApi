package com.example.weatherapi.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_tbl")
data class CurrentWeatherObject(
    @PrimaryKey
    val id: Int = 1,

    @ColumnInfo(name = "weather")
    val weather: String,
)