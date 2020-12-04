package com.uniolco.weathapp.data.db.entity.current

import com.google.gson.annotations.SerializedName

data class Main(
    val humidity: Int, // percents
    val pressure: Int, //hPa -> 1 is equal to 0.75 mmHg (миллиметры ртутного столба)
    val temp: Double, // Kelvin degrees
    @SerializedName("feels_like")
    val feelsLike: Double, // Kelvin degrees
    @SerializedName("temp_max")
    val tempMax: Double, // Kelvin degrees
    @SerializedName("temp_min")
    val tempMin: Double // Kelvin degrees
) {
    constructor() : this(0, 0, 0.0, 0.0, 0.0, 0.0)
}