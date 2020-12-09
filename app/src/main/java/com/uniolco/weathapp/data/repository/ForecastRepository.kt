package com.uniolco.weathapp.data.repository

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeather>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}