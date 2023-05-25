package com.example.weatherapi.data.model


import com.google.gson.annotations.SerializedName

data class SysModel(
    @SerializedName("country")
    val country: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("sunrise")
    val sunrise: Int? = 0,
    @SerializedName("sunset")
    val sunset: Int? = 0,
    @SerializedName("type")
    val type: Int? = 0
)