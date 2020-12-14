package com.uniolco.weathapp.ui.favorite

import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.data.repository.ForecastRepository
import com.uniolco.weathapp.internal.lazyDeferred

class FavoriteListWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    val favorites by lazyDeferred {
        forecastRepository.getAllLocations()
    }

    suspend fun deleteLocation(locations: Locations){
        forecastRepository.deleteLocation(locations)
    }
}