package com.uniolco.weathapp.data.network.response


import com.google.gson.annotations.SerializedName
import com.uniolco.weathapp.data.db.entity.forecast.FutureWeather

data class ForecastContainer(
    @SerializedName("forecastday")
    val forecastdays: List<FutureWeather>
)