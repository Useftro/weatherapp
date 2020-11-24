package com.uniolco.weathapp.ui.weather.current

import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.repository.ForecastRepository
import com.uniolco.weathapp.internal.UnitSystem
import com.uniolco.weathapp.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    // lazy so we call it only when view needs new data
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }
}