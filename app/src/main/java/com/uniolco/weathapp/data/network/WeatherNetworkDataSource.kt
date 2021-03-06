package com.uniolco.weathapp.data.network

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.FutureWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>
    val downloadedFavoriteWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String
    )

    suspend fun fetchFutureWeather(
        location: String
    )

    suspend fun fetchFavoriteWeather(
        location: String
    )
}