package com.uniolco.weathapp.data.network.response


import androidx.room.Embedded
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation


data class CurrentWeatherResponse(
    @Embedded
    val current: CurrentWeather,
    @Embedded(prefix = "location_")
    val location: WeatherLocation
)