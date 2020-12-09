package com.uniolco.weathapp.data.provider

import com.uniolco.weathapp.data.db.entity.current.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean
    suspend fun getPreferredLocationString(): String
}