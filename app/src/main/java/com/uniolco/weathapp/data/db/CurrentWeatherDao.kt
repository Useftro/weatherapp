package com.uniolco.weathapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uniolco.weathapp.data.db.entity.current.CurrentWeather
import com.uniolco.weathapp.data.db.entity.current.WeatherLocation
import com.uniolco.weathapp.data.network.response.CurrentWeatherResponse


@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // REPLACE because when updating we will always have a conflict
    // because of the same id
    fun insertOrUpdate(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather")
    fun getWeatherMetric(): LiveData<CurrentWeather> //LiveData is like observer who notifies subscribers that data has been changed

//
//
//    @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
//    fun getWeatherMImperial(): LiveData<ImperialCurrentWeather>

}