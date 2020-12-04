package com.uniolco.weathapp.data.db.entity.onecall


import com.google.gson.annotations.SerializedName

data class Hourly(
    val clouds: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val dt: Int,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Int,
    val pop: Int,
    val pressure: Int,
    val rain: Rain,
    val temp: Double,
    val uvi: Int,
    val visibility: Int,
    val weather: List<Weather>,
    @SerializedName("wind_deg")
    val windDeg: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double
)