package com.uniolco.weathapp.ui.weather.future.list

import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.repository.ForecastRepository
import com.uniolco.weathapp.internal.lazyDeferred
import com.uniolco.weathapp.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : WeatherViewModel(forecastRepository) {

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now())
    }
}