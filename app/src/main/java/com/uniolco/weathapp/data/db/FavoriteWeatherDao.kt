package com.uniolco.weathapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.favorite.FavoriteEntry
import com.uniolco.weathapp.data.db.entity.favorite.Locations

@Dao
interface FavoriteWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(favoriteEntry: FavoriteEntry)

    @Query("SELECT * FROM favorite_weather")
    fun getAllFavorites(): List<FavoriteEntry>

    @Query("SELECT * FROM favorite_weather WHERE weather_location_name LIKE :location")
    fun getExactFavorite(location: String): FavoriteEntry

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocations(weatherLocation: Locations)

    @Query("SELECT * from favorite_locations WHERE userEmail LIKE :userEm")
    fun getAllLocations(userEm: String): LiveData<List<Locations>>

    @Query("DELETE FROM favorite_locations")
    fun deleteAllLocations()

    @Suppress("AndroidUnresolvedRoomSqlReference")
    @Query("SELECT id_loc FROM favorite_locations WHERE name = :tableName")
    fun getIdNumber(tableName: String): Long?

    @Update
    fun updateWeatherInLocation(currentWeather: CurrentWeather)

    @Delete
    fun deleteLocation(locations: Locations)
}