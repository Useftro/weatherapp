package com.uniolco.weathapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uniolco.weathapp.data.db.entity.current.WEATHER_LOCATION_ID
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation

@Dao
interface WeatherLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(weatherLocation: WeatherLocation)

    @Query("select * from weather_location where id_w = $WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<WeatherLocation>

    @Query("select * from weather_location where id_w = $WEATHER_LOCATION_ID")
    fun getLocationNonLive(): WeatherLocation?
}