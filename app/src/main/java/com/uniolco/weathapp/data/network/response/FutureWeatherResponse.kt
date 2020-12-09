package com.uniolco.weathapp.data.network.response


import com.google.gson.annotations.SerializedName
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation
import com.uniolco.weathapp.data.db.entity.forecast.Alert

data class FutureWeatherResponse(
    val alert: Alert,
    @SerializedName("forecast")
    val futureWeatherEntries: ForecastContainer,
    val location: WeatherLocation
)