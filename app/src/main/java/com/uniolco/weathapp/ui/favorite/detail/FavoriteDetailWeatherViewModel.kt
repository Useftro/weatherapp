package com.uniolco.weathapp.ui.favorite.detail

import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.repository.ForecastRepository
import com.uniolco.weathapp.internal.lazyDeferred

class FavoriteDetailWeatherViewModel(
    private val location: String,
    private val forecastRepository: ForecastRepository
    ) : ViewModel() {

    val exactWeather by lazyDeferred {
        forecastRepository.getExactFavorite(location)
    }
}