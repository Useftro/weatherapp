package com.uniolco.weathapp.ui.weather.future.detail

import com.uniolco.weathapp.data.repository.ForecastRepository
import com.uniolco.weathapp.internal.lazyDeferred
import com.uniolco.weathapp.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModel(
    private val detailDate: LocalDate,
    private val forecastRepository: ForecastRepository
) : WeatherViewModel(forecastRepository) {

    val weather by lazyDeferred {
        forecastRepository.getFutureWeatherByDate(detailDate)
    }

}