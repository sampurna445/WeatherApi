package com.example.weatherapi.data.model


import com.google.gson.annotations.SerializedName

data class WeatherModelX(
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("icon")
    val icon: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("main")
    val main: String? = ""
)