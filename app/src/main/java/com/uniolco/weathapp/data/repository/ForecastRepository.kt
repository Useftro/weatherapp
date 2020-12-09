package com.uniolco.weathapp.data.repository

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation
import com.uniolco.weathapp.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeather>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
    suspend fun getFutureWeatherList(startDate: LocalDate): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>
}