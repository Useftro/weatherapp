package com.uniolco.weathapp.ui.base

import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.repository.ForecastRepository
import com.uniolco.weathapp.internal.lazyDeferred

abstract class WeatherViewModel(
    private val forecastRepository: ForecastRepository
): ViewModel() {
    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}