package com.uniolco.weathapp.ui.favorite.list

import androidx.lifecycle.ViewModel
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.data.repository.ForecastRepository
import com.uniolco.weathapp.internal.lazyDeferred

class FavoriteListWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    private val userEmail: String
) : ViewModel() {


    val favorites by lazyDeferred {
        forecastRepository.getAllLocations(userEmail)
    }

    suspend fun deleteLocation(locations: Locations){
        forecastRepository.deleteLocation(locations)
    }

    suspend fun deleteAllLocations(){
        forecastRepository.deleteAllLocations()
    }
}