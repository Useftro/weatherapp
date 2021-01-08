package com.uniolco.weathapp.data.repository

import androidx.lifecycle.LiveData
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation
import com.uniolco.weathapp.data.db.entity.favorite.FavoriteEntry
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.data.db.unitlocalized.future.detailed.UnitSpecificDetailedFutureWeatherEntry
import com.uniolco.weathapp.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.uniolco.weathapp.data.firebase.User
import org.threeten.bp.LocalDate

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeather>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
    suspend fun getFutureWeatherList(startDate: LocalDate): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>
    suspend fun getFutureWeatherByDate(date: LocalDate): LiveData<out UnitSpecificDetailedFutureWeatherEntry>
    suspend fun getFavorites(locations: String): List<FavoriteEntry>

    suspend fun getAllLocations(userEmail: String): LiveData<out List<Locations>>
    suspend fun insertLocations(weatherLocation: Locations)
    suspend fun deleteLocation(locations: Locations)
    suspend fun deleteAllLocations()

    suspend fun getExactFavorite(location: String): FavoriteEntry

    suspend fun insertFavoriteEntry(favoriteEntry: FavoriteEntry)

    /*suspend */fun getUser(email: String): LiveData<User>
    suspend fun insertUser(user: User)
}