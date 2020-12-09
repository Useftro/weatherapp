package com.uniolco.weathapp.data.network

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String
    )

/*    suspend fun fetchOneCallWeather(
        latitude: String,
        longitude: String
    )*/
}