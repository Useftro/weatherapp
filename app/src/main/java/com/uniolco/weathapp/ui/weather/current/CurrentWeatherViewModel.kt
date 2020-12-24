package com.uniolco.weathapp.ui.weather.current

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.data.firebase.User
import com.uniolco.weathapp.data.repository.ForecastRepository
import com.uniolco.weathapp.internal.UnitSystem
import com.uniolco.weathapp.internal.lazyDeferred
import com.uniolco.weathapp.ui.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : WeatherViewModel(forecastRepository) {
    private val unitSystem = UnitSystem.METRIC

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    // lazy so we call it only when view needs new data
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }

    suspend fun insertIntoFavorite(favoriteLocation: Locations){
        forecastRepository.insertLocations(favoriteLocation)
    }

    suspend fun insertOrUpdateUser(user: User){
        forecastRepository.insertUser(user)
    }

    fun getUser(email: String): LiveData<User>{
        return forecastRepository.getUser(email)
    }
}