package com.uniolco.weathapp.data.repository

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.db.entity.current.Coord
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse
import com.uniolco.weathapp.data.network.response.OneCallServerResponse

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherResponse>
    suspend fun getOneCallWeather(): LiveData<OneCallServerResponse>
    suspend fun getCurrentLocation(): LiveData<Coord>
    suspend fun getCurrentTime(): Long
}