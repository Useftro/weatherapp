package com.uniolco.weathapp.data.repository

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherResponse>
}