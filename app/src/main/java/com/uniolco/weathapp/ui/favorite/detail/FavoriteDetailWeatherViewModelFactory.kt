package com.uniolco.weathapp.ui.favorite.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uniolco.weathapp.data.repository.ForecastRepository

class FavoriteDetailWeatherViewModelFactory(
    private val location: String,
    private val forecastRepository: ForecastRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoriteDetailWeatherViewModel(location, forecastRepository) as T
    }
}