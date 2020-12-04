package com.uniolco.weathapp.data.network

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.OneCallServerResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadedOneCallWeather: LiveData<OneCallServerResponse>

    suspend fun fetchCurrentWeather(
        location: String
    )

    suspend fun fetchOneCallWeather(
        latitude: String,
        longitude: String
    )
}