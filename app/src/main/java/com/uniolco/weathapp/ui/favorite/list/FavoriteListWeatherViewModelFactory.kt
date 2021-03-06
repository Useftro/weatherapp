package com.uniolco.weathapp.ui.favorite.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uniolco.weathapp.data.repository.ForecastRepository

class FavoriteListWeatherViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val userEmail: String
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoriteListWeatherViewModel(forecastRepository, userEmail) as T
    }
}