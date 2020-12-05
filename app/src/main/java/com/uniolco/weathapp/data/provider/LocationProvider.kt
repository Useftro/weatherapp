package com.uniolco.weathapp.data.provider

import com.uniolco.weathapp.data.db.entity.current.Coord
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse

interface LocationProvider {
    suspend fun hasLocationChanged(
        lastWeatherLocation: Coord
                                   ): Boolean
    suspend fun getPreferredLocationString(): String
}